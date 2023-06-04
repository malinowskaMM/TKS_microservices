package p.lodz.tks.user.service.rest.controller.adapters;

import com.nimbusds.jose.JOSEException;
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

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Response ping() {
        return Response.ok().build();
    }

    @PUT
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
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
    public Response getUsers() {
        return Response.ok().entity(userUseCase.getAllUsers()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getUsersByPartOfLogin(String partOfLogin) {
        return Response.ok().entity(userUseCase.findClientsByLoginPart(partOfLogin)).build();
    }

    @DELETE
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
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
    public Response getUserByLogin(@PathParam("login") String login) throws UserWithGivenIdNotFound {
        if (userUseCase.findUserByLogin(login) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(userUseCase.findUserByLogin(login)).build();
    }

    @PUT
    @Path("/client/activate/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
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

