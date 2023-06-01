package p.lodz.tks.user.service.rest.controller.adapters;

import com.nimbusds.jose.JOSEException;
import org.json.JSONObject;
import p.lodz.tks.user.service.application.core.application.services.auth.JwsGenerator;
import p.lodz.tks.user.service.application.core.domain.model.exceptions.*;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.user.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.user.service.application.core.domain.model.model.user.manager.Manager;
import p.lodz.tks.user.service.rest.controller.dto.auth.PasswordChangeDto;
import p.lodz.tks.user.service.rest.controller.dto.user.AdminDto;
import p.lodz.tks.user.service.rest.controller.dto.user.ClientDto;
import p.lodz.tks.user.service.rest.controller.dto.user.ManagerDto;
import p.lodz.tks.user.service.rest.controller.mappers.UserDtoMapper;
import p.lodz.tks.user.service.user.UserUseCase;
import p.lodz.tks.user.service.user.rabbitmq.adapter.event.ClientCreatedEvent;
import p.lodz.tks.user.service.user.rabbitmq.adapter.messaging.ClientProducer;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.UUID;

@Path("/users")
public class UserResourceAdapter {

    @Inject
    UserUseCase userUseCase;

    @Inject
    UserDtoMapper userDtoMapper;

    @Inject
    JwsGenerator jwsGenerator;

    @Inject
    private ClientProducer clientProducer;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/passwordChange")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response changeUserPassword(@NotNull PasswordChangeDto passwordChangeDto) {
        if (passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
            userUseCase.changePassword(passwordChangeDto.getOldPassword(), passwordChangeDto.getNewPassword());
            return Response.ok().build();
        } else {
            throw new PasswordMatchFailed("New password and new confrim password do not match");
        }

    }

    @GET
    @Path("/ping")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT", "NONE"})
    public Response ping() {
        return Response.ok().build();
    }

    @POST
    @Path("/client")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT", "NONE"})
    public Response createClient(@Valid ClientDto clientDto) throws ClientValidationFailed {
        Client client = (Client) userDtoMapper.toUser(clientDto);
        client = userUseCase.registerClient(client.getFirstName(), client.getLastName(), client.getPersonalId(), client.getAddress(), client.getLogin(), client.getPassword());

        clientProducer.produce(new ClientCreatedEvent(client));

        return Response.ok().entity(client).build();
    }

    @POST
    @Path("/admin")
    @RolesAllowed({"ADMIN"})
    public Response createAdmin(@Valid AdminDto adminDto) throws AdminValidationFailed {
        Admin admin = (Admin) userDtoMapper.toUser(adminDto);
        admin = userUseCase.registerAdmin(admin.getLogin(), admin.getPassword());
        return Response.ok().entity(admin).build();
    }

    @POST
    @Path("/manager")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response createManager(@Valid ManagerDto managerDto) throws ManagerValidationFailed {
        Manager manager = (Manager) userDtoMapper.toUser(managerDto);
        manager = userUseCase.registerManager(manager.getLogin(), manager.getPassword());
        return Response.ok().entity(manager).build();
    }

    @PUT
    @Path("/client/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response updateClient(@PathParam("uuid") UUID id, @Valid ClientDto clientDto, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userUseCase.getUserById(id) == null) {
            return Response.status(404).build();
        }
        Client client = (Client) userDtoMapper.toUser(clientDto);
        userUseCase.updateUser(id, jws, client.getFirstName(), client.getLastName(), client.getAddress(), client.getLogin(), client.getPassword(), client.getAccessLevel());

        clientProducer.produce(new ClientCreatedEvent(client));

        return Response.ok().build();
    }

    @PUT
    @Path("/admin/{uuid}")
    @RolesAllowed({"ADMIN"})
    public Response updateAdmin(@PathParam("uuid") UUID id, @Valid AdminDto adminDto, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userUseCase.getUserById(id) == null) {
            return Response.status(404).build();
        }
        Admin admin = (Admin) userDtoMapper.toUser(adminDto);
        userUseCase.updateUser(id, jws, null, null, null, admin.getLogin(), admin.getPassword(), admin.getAccessLevel());
        return Response.ok().build();
    }

    @PUT
    @Path("/manager/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response updateUser(@PathParam("uuid") UUID id, @Valid ManagerDto managerDto, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userUseCase.getUserById(id) == null) {
            return Response.status(404).build();
        }
        Manager manager = (Manager) userDtoMapper.toUser(managerDto);
        userUseCase.updateUser(id, jws, null, null, null, manager.getLogin(), manager.getPassword(), manager.getAccessLevel());
        return Response.ok().build();
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
            userUseCase.getUserById(userId);
            userUseCase.deleteUser(userId);
        } catch (UserWithGivenIdNotFound e) {
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
    @Path("/client/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getClient(@PathParam("uuid") UUID userId) throws UserWithGivenIdNotFound {
        if (userUseCase.getClientById(userId) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(userUseCase.getClientById(userId)).build();
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

