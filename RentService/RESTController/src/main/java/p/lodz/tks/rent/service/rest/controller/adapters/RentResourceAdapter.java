package p.lodz.tks.rent.service.rest.controller.adapters;

import com.nimbusds.jose.JOSEException;

import javax.ejb.Stateless;
import javax.ws.rs.*;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.json.JSONObject;
import p.lodz.tks.rent.service.application.core.application.services.services.auth.JwsGenerator;
import p.lodz.tks.rent.service.rest.controller.dto.rent.RentDto;
import p.lodz.tks.rent.service.application.core.domain.model.model.rent.Rent;
import p.lodz.tks.rent.service.rent.RentUseCase;
import p.lodz.tks.rent.service.rest.controller.mappers.RentDtoMapper;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.UUID;

@Path("/rents")
@Stateless
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
    @Timed(name = "getAllRent",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get all rents")
    @Counted(name = "getAllRentsInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getRents() {
        return Response.ok().entity(rentUseCase.getRents()).build();
    }

    @GET
    @Path("/room")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "getFreeRooms",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get free rooms")
    @Counted(name = "getFreeRoomsInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getFreeRooms() {
        return Response.ok().entity(rentUseCase.getCurrentFreeRooms()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/room/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "getRentsByRoom",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get rents by room")
    @Counted(name = "getRentsByRoomInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getRentsByRoom(@PathParam("uuid") UUID roomId) {
        return Response.ok().entity(rentUseCase.getRentsByRoomId(roomId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/client/{login}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "getRentsByClient",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get rents by client")
    @Counted(name = "getRentsByClientInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getRentsByClient(@PathParam("login") String login) {
        return Response.ok().entity(rentUseCase.getRentsByClientId(login)).build();
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/startDate")
    @RolesAllowed({"ADMIN", "MANAGER"})
    @Timed(name = "getRentsByStartDate",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get rents by start date")
    @Counted(name = "getRentsByStartDateRentsInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getRentsByStartDate(@QueryParam("startDate") String startDate) {
        LocalDateTime date = LocalDateTime.parse(startDate);
        return Response.ok().entity(rentUseCase.getRentsByStartDate(date)).build();
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/endDate")
    @RolesAllowed({"ADMIN", "MANAGER"})
    @Timed(name = "getRentsByEndDate",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get rents by end date")
    @Counted(name = "geRentsByEndDateInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getRentsByEndDate(@QueryParam("endDate") String endDate) {
        LocalDateTime date = LocalDateTime.parse(endDate);
        return Response.ok().entity(rentUseCase.getRentsByEndDate(date)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/archived")
    @RolesAllowed({"ADMIN", "MANAGER"})
    @Timed(name = "getPastRents",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get past rents")
    @Counted(name = "getPastRentsInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getPastRents() {
        return Response.ok().entity(rentUseCase.getPastRents()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current/client/{login}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "getCurrentRentsByClientId",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get current rents by client ID")
    @Counted(name = "getCurrentRentsByClientIdInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getCurrentRentsByClientId(@PathParam("login") String login) {
        return Response.ok().entity(rentUseCase.getCurrentRentsByClientId(login)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/archived/client/{login}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    @Timed(name = "getPastRentsByClientId",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get past rents by client ID")
    @Counted(name = "getPastRentsByClientIdInvocations",
            absolute = true,
            description = "Number of invocations")
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
    @Timed(name = "getPastRentsByRoomId",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get past rents by room ID")
    @Counted(name = "getPastRentsByRoomIdInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getPastRentsByRoomId(@PathParam("uuid") UUID roomId) {
        return Response.ok().entity(rentUseCase.getPastRentsByRoomId(roomId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "getCurrentRents",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get current rents")
    @Counted(name = "getCurrentRentsInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getCurrentRents() {
        return Response.ok().entity(rentUseCase.getCurrentRents()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "rentRoom",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to rent a room")
    @Counted(name = "getRentRoomInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response rentRoom(@Valid RentDto rentDto) {
        Rent rent = rentDtoMapper.toRent(rentDto);
        Rent rentResult = rentUseCase.rentRoom(rent.getRoomId(), rent.getBeginTime(), rent.getEndTime());
        return Response.ok().entity(rentResult).build();
    }

    @PUT
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "endRent",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to end a rent")
    @Counted(name = "getEndRentInvocations",
            absolute = true,
            description = "Number of invocations")
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
    @Timed(name = "getRent",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to get rent")
    @Counted(name = "getRentInvocations",
            absolute = true,
            description = "Number of invocations")
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
    @Timed(name = "deleteRent",
            tags = {"method=rent"},
            absolute = true,
            description = "Time to delete a rent")
    @Counted(name = "deleteRentInvocations",
            absolute = true,
            description = "Number of invocations")
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
