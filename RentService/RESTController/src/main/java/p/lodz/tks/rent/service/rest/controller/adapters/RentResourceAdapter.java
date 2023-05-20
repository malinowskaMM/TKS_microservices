package p.lodz.tks.rent.service.rest.controller.adapters;

import com.nimbusds.jose.JOSEException;
import jakarta.ws.rs.*;
import org.json.JSONObject;
import p.lodz.tks.rent.service.application.core.application.services.services.auth.JwsGenerator;
import p.lodz.tks.rent.service.application.core.domain.model.dto.rent.RentDto;
import p.lodz.tks.rent.service.application.core.domain.model.model.rent.Rent;
import p.lodz.tks.rent.service.rent.RentUseCase;
import p.lodz.tks.rent.service.rest.controller.mappers.RentDtoMapper;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.UUID;

@Path("/rents")
public class RentResourceAdapter {

    @Inject
    private RentUseCase rentUseCase;;

    @Inject
    private RentDtoMapper rentDtoMapper;

    @Inject
    JwsGenerator jwsGenerator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getRents() {
        return Response.ok().entity(rentUseCase.getRents()).build();
    }

    @GET
    @Path("/room")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getFreeRooms() {
        return Response.ok().entity(rentUseCase.getCurrentFreeRooms()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/room/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getRentsByRoom(@PathParam("uuid") UUID roomId) {
        return Response.ok().entity(rentUseCase.getRentsByRoomId(roomId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/client/{login}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getRentsByClient(@PathParam("login") String login) {
        return Response.ok().entity(rentUseCase.getRentsByClientId(login)).build();
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/startDate")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getRentsByStartDate(@QueryParam("startDate") String startDate) {
        LocalDateTime date = LocalDateTime.parse(startDate);
        return Response.ok().entity(rentUseCase.getRentsByStartDate(date)).build();
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/endDate")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getRentsByEndDate(@QueryParam("endDate") String endDate) {
        LocalDateTime date = LocalDateTime.parse(endDate);
        return Response.ok().entity(rentUseCase.getRentsByEndDate(date)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/archived")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getPastRents() {
        return Response.ok().entity(rentUseCase.getPastRents()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current/client/{login}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getCurrentRentsByClientId(@PathParam("login") String login) {
        return Response.ok().entity(rentUseCase.getCurrentRentsByClientId(login)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/archived/client/{login}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getPastRentsByClientId(@PathParam("login") String login) {
        return Response.ok().entity(rentUseCase.getPastRentsByClientId(login)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current/room/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getCurrentRentsByRoomId(@PathParam("uuid") UUID roomId) {
        return Response.ok().entity(rentUseCase.getCurrentRentsByRoomId(roomId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/archived/room/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getPastRentsByRoomId(@PathParam("uuid") UUID roomId) {
        return Response.ok().entity(rentUseCase.getPastRentsByRoomId(roomId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getCurrentRents() {
        return Response.ok().entity(rentUseCase.getCurrentRents()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response rentRoom(@Valid RentDto rentDto) {
        Rent rent = rentDtoMapper.toRent(rentDto);
        Rent rentResult = rentUseCase.rentRoom(rent.getRoomId(), rent.getBeginTime(), rent.getEndTime());
        return Response.ok().entity(rentResult).build();
    }

    @PUT
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response endRent(@PathParam("uuid") UUID rentId, @Context HttpServletRequest request) throws ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if(rentUseCase.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        rentUseCase.endRoomRent(rentId, jws);
        return Response.ok().build();
    }

    @GET
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getRent(@PathParam("uuid") UUID rentId) throws JOSEException {
        if(rentUseCase.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(rentUseCase.getRent(rentId))
                .header("ETag", getJwsFromRent(rentId)).build();
    }

    @DELETE
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response deleteRent(@PathParam("uuid") UUID rentId) {
        if(rentUseCase.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        rentUseCase.removeRent(rentId);
        return Response.ok().build();
    }

    public String getJwsFromRent(UUID id) throws NotFoundException, JOSEException {
        Rent rent = rentUseCase.getRent(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", rent.getId().toString());
        return this.jwsGenerator.generateJws(jsonObject.toString());
    }
}
