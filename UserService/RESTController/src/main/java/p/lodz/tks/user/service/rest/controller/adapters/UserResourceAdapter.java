package p.lodz.tks.user.service.rest.controller.adapters;

import com.nimbusds.jose.JOSEException;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.json.JSONObject;
import p.lodz.tks.user.service.application.core.application.services.auth.JwsGenerator;
import p.lodz.tks.user.service.application.core.domain.model.exceptions.ManagerValidationFailed;
import p.lodz.tks.user.service.application.core.domain.model.exceptions.PasswordMatchFailed;
import p.lodz.tks.user.service.application.core.domain.model.exceptions.UserWithGivenIdNotFound;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.message.queue.Publisher;
import p.lodz.tks.user.service.rest.controller.dto.auth.PasswordChangeDto;
import p.lodz.tks.user.service.rest.controller.dto.user.UserDto;
import p.lodz.tks.user.service.rest.controller.mappers.UserDtoMapper;
import p.lodz.tks.user.service.user.UserUseCase;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

@Path("/users")
public class UserResourceAdapter {

    @Inject
    UserUseCase userUseCase;

    @Inject
    private Publisher publisher;

    @Inject
    JwsGenerator jwsGenerator;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/passwordChange")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "changeUserPassword",
            tags = {"method=user"},
            absolute = true,
            description = "Time to change user password")
    @Counted(name = "changeUserPasswordInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response changeUserPassword(@NotNull PasswordChangeDto passwordChangeDto) {
        if (passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
            userUseCase.changePassword(passwordChangeDto.getOldPassword(), passwordChangeDto.getNewPassword());
            return Response.ok().build();
        } else {
            throw new PasswordMatchFailed("New password and new confirm password do not match");
        }
    }

    @GET
    @Path("/ping")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT", "NONE"})
    @Timed(name = "ping",
            tags = {"method=ping"},
            absolute = true,
            description = "Time to ping")
    @Counted(name = "pingInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response ping() {
        return Response.ok().build();
    }

    @PUT
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "updateUser",
            tags = {"method=user"},
            absolute = true,
            description = "Time to update user")
    @Counted(name = "updateUserInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response updateUser(@PathParam("uuid") UUID id, @Valid UserDto userDto, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, IOException {
        if (userUseCase.getUserById(id) == null) {
            return Response.status(404).build();
        }
        userDto.setUuid(id.toString());
        if (userDto.getAccessLevel().equals("CLIENT")) {
            publisher.updateUser(Publisher.Serialization
                    .clientToJsonString(
                            UserDtoMapper.toUser(userDto),
                            userDto.getPersonalId(),
                            userDto.getFirstName(),
                            userDto.getLastName(),
                            userDto.getAddress()
                    )
            );
        } else if (userDto.getAccessLevel().equals("ADMIN") || userDto.getAccessLevel().equals("MANAGER")) {
            publisher.updateUser(Publisher.Serialization.userToJsonString(UserDtoMapper.toUser(userDto)));
        }

        return Response.ok().build();
    }

    @POST
    @RolesAllowed({"ADMIN", "MANAGER", "NONE"})
    @Timed(name = "createUser",
            tags = {"method=user"},
            absolute = true,
            description = "Time to create user")
    @Counted(name = "createUserInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response createUser(@Valid UserDto userDto) throws ManagerValidationFailed, IOException {
        if (userDto.getAccessLevel().equals("CLIENT")) {
            publisher.createUser(Publisher.Serialization
                    .clientToJsonString(
                            UserDtoMapper.toUser(userDto),
                            userDto.getPersonalId(),
                            userDto.getFirstName(),
                            userDto.getLastName(),
                            userDto.getAddress()
                    ));
        } else if (userDto.getAccessLevel().equals("MANAGER") || userDto.getAccessLevel().equals("ADMIN")) {
            publisher.createUser(Publisher.Serialization.userToJsonString(UserDtoMapper.toUser(userDto)));
        }
        return Response.status(201).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER"})
    @Timed(name = "getUsers",
            tags = {"method=user"},
            absolute = true,
            description = "Time to get users")
    @Counted(name = "getUsersInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getUsers() {
        return Response.ok().entity(userUseCase.getAllUsers()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "getUsersByPartOfLogin",
            tags = {"method=user"},
            absolute = true,
            description = "Time to get users by part of login")
    @Counted(name = "getUsersByPartOfLoginInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getUsersByPartOfLogin(String partOfLogin) {
        return Response.ok().entity(userUseCase.findClientsByLoginPart(partOfLogin)).build();
    }

    @DELETE
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    @Timed(name = "deleteUser",
            tags = {"method=user"},
            absolute = true,
            description = "Time to delete user")
    @Counted(name = "deleteUserInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response deleteUser(@PathParam("uuid") UUID userId) throws UserWithGivenIdNotFound {
        try {
            publisher.deleteUser(userUseCase.getUserById(userId).getLogin());
        } catch (UserWithGivenIdNotFound | IOException e) {
            return Response.status(404).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    @Timed(name = "getUser",
            tags = {"method=user"},
            absolute = true,
            description = "Time to get user")
    @Counted(name = "getUserInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getUser(@PathParam("uuid") UUID userId) throws UserWithGivenIdNotFound, JOSEException {
        if (userUseCase.getUserById(userId) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(userUseCase.getUserById(userId))
                .header("ETag", getJwsFromUser(userId)).build();
    }

    @GET
    @Path("/findByLogin/{login}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    @Timed(name = "getUserByLogin",
            tags = {"method=user"},
            absolute = true,
            description = "Time to get user by login")
    @Counted(name = "getUserByLoginInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getUserByLogin(@PathParam("login") String login) throws UserWithGivenIdNotFound {
        if (userUseCase.findUserByLogin(login) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(userUseCase.findUserByLogin(login)).build();
    }

    @PUT
    @Path("/client/activate/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    @Timed(name = "activateUser",
            tags = {"method=user"},
            absolute = true,
            description = "Time to activate user")
    @Counted(name = "activateUserInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response activateUser(@PathParam("uuid") UUID userId, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userUseCase.getUserById(userId) == null) {
            return Response.status(404).build();
        }
        userUseCase.activateUser(userId, jws);
        return Response.ok().build();
    }

    @PUT
    @Path("/client/deactivate/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    @Timed(name = "deactivateUser",
            tags = {"method=user"},
            absolute = true,
            description = "Time to deactivate user")
    @Counted(name = "deactivateUserInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response deactivateUser(@PathParam("uuid") UUID userId, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userUseCase.getUserById(userId) == null) {
            return Response.status(404).build();
        }
        userUseCase.deactivateUser(userId, jws);
        return Response.ok().build();
    }

    public String getJwsFromUser(UUID id) throws NotFoundException, JOSEException {
        User user = userUseCase.getUserByIdInside(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", user.getUuid().toString());
        return this.jwsGenerator.generateJws(jsonObject.toString());
    }
}

