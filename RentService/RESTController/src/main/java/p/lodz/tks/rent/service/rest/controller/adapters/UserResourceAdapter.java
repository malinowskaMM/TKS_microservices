package p.lodz.tks.rent.service.rest.controller.adapters;

import com.nimbusds.jose.JOSEException;
import javax.validation.Valid;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.json.JSONObject;
import p.lodz.tks.rent.service.application.core.application.services.services.auth.JwsGenerator;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.UserWithGivenIdNotFound;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.manager.Manager;
import p.lodz.tks.rent.service.rest.controller.dto.user.AdminDto;
import p.lodz.tks.rent.service.rest.controller.dto.user.ClientDto;
import p.lodz.tks.rent.service.rest.controller.dto.user.ManagerDto;
import p.lodz.tks.rent.service.rest.controller.dto.user.mapper.UserDtoMapper;
import p.lodz.tks.rent.service.user.UserUseCase;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
    JwsGenerator jwsGenerator;

    @Inject
    UserDtoMapper userDtoMapper;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "NONE"})
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

    @PUT
    @Path("/client/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "updateClient",
            tags = {"method=client"},
            absolute = true,
            description = "Time to update client")
    @Counted(name = "updateClientInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response updateClient(@PathParam("uuid") UUID id, @Valid ClientDto clientDto, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userUseCase.getUserById(id) == null) {
            return Response.status(404).build();
        }
        Client client = (Client) userDtoMapper.toUser(clientDto);
        userUseCase.updateUser(id, client.getFirstName(), client.getLastName(), client.getAddress(), client.getLogin(), client.getPassword(), client.getAccessLevel());
        return Response.ok().build();
    }

    @PUT
    @Path("/admin/{uuid}")
    @RolesAllowed({"ADMIN"})
    @Timed(name = "updateAdmin",
            tags = {"method=admin"},
            absolute = true,
            description = "Time to update admin")
    @Counted(name = "updateAdminInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response updateAdmin(@PathParam("uuid") UUID id, @Valid AdminDto adminDto, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException, ParseException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userUseCase.getUserById(id) == null) {
            return Response.status(404).build();
        }
        Admin admin = (Admin) userDtoMapper.toUser(adminDto);
        userUseCase.updateUser(id, null, null, null, admin.getLogin(), admin.getPassword(), admin.getAccessLevel());
        return Response.ok().build();
    }

    @PUT
    @Path("/manager/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "updateManager",
            tags = {"method=manager"},
            absolute = true,
            description = "Time to update manager")
    @Counted(name = "updateManagerInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response updateManager(@PathParam("uuid") UUID id, @Valid ManagerDto managerDto, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userUseCase.getUserById(id) == null) {
            return Response.status(404).build();
        }
        Manager manager = (Manager) userDtoMapper.toUser(managerDto);
        userUseCase.updateUser(id,null, null, null, manager.getLogin(), manager.getPassword(), manager.getAccessLevel());
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

    public String getJwsFromUser(UUID id) throws NotFoundException, JOSEException {
        User user = userUseCase.getUserByIdInside(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", user.getUuid().toString());
        return this.jwsGenerator.generateJws(jsonObject.toString());
    }
}

