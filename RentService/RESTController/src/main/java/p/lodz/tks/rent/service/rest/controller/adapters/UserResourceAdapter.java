package p.lodz.tks.rent.service.rest.controller.adapters;

import com.nimbusds.jose.JOSEException;
import org.json.JSONObject;
import p.lodz.tks.rent.service.application.core.application.services.services.auth.JwsGenerator;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.UserWithGivenIdNotFound;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;
import p.lodz.tks.rent.service.user.UserUseCase;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/users")
public class UserResourceAdapter {

    @Inject
    UserUseCase userUseCase;

    @Inject
    JwsGenerator jwsGenerator;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "NONE"})
    public Response getUsers() {
        return Response.ok().entity(userUseCase.getAllUsers()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getUsersByPartOfLogin(String partOfLogin) {
        return Response.ok().entity(userUseCase.findClientsByLoginPart(partOfLogin)).build();
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

    public String getJwsFromUser(UUID id) throws NotFoundException, JOSEException {
        User user = userUseCase.getUserByIdInside(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", user.getUuid().toString());
        return this.jwsGenerator.generateJws(jsonObject.toString());
    }
}

