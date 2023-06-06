package p.lodz.tks.rent.service.rest.controller.adapters;

import com.nimbusds.jose.JOSEException;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.json.JSONObject;
import p.lodz.tks.rent.service.application.core.application.services.services.auth.JwsGenerator;
import p.lodz.tks.rent.service.rest.controller.dto.room.RoomDto;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.RoomValidationFailed;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.RoomWithGivenIdNotFound;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;
import p.lodz.tks.rent.service.rest.controller.mappers.RoomDtoMapper;
import p.lodz.tks.rent.service.room.RoomUseCase;


import java.text.ParseException;
import java.util.UUID;

@Path("/rooms")
public class  RoomResourceAdapter {

    @Inject
    JwsGenerator jwsGenerator;

    @Inject
    private RoomUseCase roomUseCase;

    @Inject
    private RoomDtoMapper roomDtoMapper;

    @GET
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "getRooms",
            tags = {"method=room"},
            absolute = true,
            description = "Time to get rooms")
    @Counted(name = "getRoomsInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getRooms() {
        return Response.ok().entity(roomUseCase.getAllRooms()).build();
    }

    @GET
    @Path("/test")
    public Response testMsg() {
        String msg = "DZIALA";
        return Response.ok().entity(msg).build();
    }

    @RolesAllowed({"ADMIN"})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(name = "createRoom",
            tags = {"method=room"},
            absolute = true,
            description = "Time to create room")
    @Counted(name = "createRoomInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response createRoom(@Valid RoomDto roomDto) throws RoomValidationFailed {
        Room room = roomDtoMapper.toRoom(roomDto);
        Room result = roomUseCase.createRoom(room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
        return Response.ok().entity(result).build();
    }

    @DELETE
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN"})
    @Timed(name = "deleteRoom",
            tags = {"method=room"},
            absolute = true,
            description = "Time to delete room")
    @Counted(name = "deleteRoomInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response deleteRoom(@PathParam("uuid") UUID roomId) throws RoomWithGivenIdNotFound {
        if(roomUseCase.getRoomById(roomId) == null) {
            return Response.status(404).build();
        }
        roomUseCase.removeRoom(roomId);
        return Response.ok().build();
    }

    @GET
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Timed(name = "getRoom",
            tags = {"method=room"},
            absolute = true,
            description = "Time to get room")
    @Counted(name = "getRoomInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getRoom(@PathParam("uuid") UUID roomId) throws RoomWithGivenIdNotFound, JOSEException {
        if(roomUseCase.getRoomById(roomId) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(roomUseCase.getRoomById(roomId)).header("ETag", getJwsFromRoom(roomId)).build();
    }

    @PUT
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(name = "updateRoom",
            tags = {"method=room"},
            absolute = true,
            description = "Time to update room")
    @Counted(name = "updateRoomInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response updateRoom(@PathParam("uuid") UUID roomId, RoomDto roomDto, @Context HttpServletRequest request) throws RoomWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if(roomUseCase.getRoomById(roomId) == null) {
            return Response.status(404).build();
        }
        Room room = roomDtoMapper.toRoom(roomDto);
        roomUseCase.updateRoom(roomId, jws, room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
        return Response.ok().build();
    }

    public String getJwsFromRoom(UUID id) throws NotFoundException, JOSEException {
        Room room = roomUseCase.getRoomById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", room.getUuid().toString());
        return this.jwsGenerator.generateJws(jsonObject.toString());
    }
}
