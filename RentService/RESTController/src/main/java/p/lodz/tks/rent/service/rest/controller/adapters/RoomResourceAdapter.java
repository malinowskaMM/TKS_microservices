package p.lodz.tks.rent.service.rest.controller.adapters;

import com.nimbusds.jose.JOSEException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import p.lodz.tks.rent.service.application.core.application.services.services.auth.JwsGenerator;
import p.lodz.tks.rent.service.application.core.domain.model.dto.room.RoomDto;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.RoomValidationFailed;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.RoomWithGivenIdNotFound;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;
import p.lodz.tks.rent.service.rest.controller.mappers.RoomDtoMapper;
import p.lodz.tks.rent.service.room.RoomUseCase;


import java.text.ParseException;
import java.util.UUID;

@Path("/rooms")
public class RoomResourceAdapter {

    @Inject
    JwsGenerator jwsGenerator;

    @Inject
    private RoomUseCase roomUseCase;

    @Inject
    private RoomDtoMapper roomDtoMapper;

    @GET
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Produces(MediaType.APPLICATION_JSON)
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
    public Response createRoom(@Valid RoomDto roomDto) throws RoomValidationFailed {
        Room room = roomDtoMapper.toRoom(roomDto);
        Room result = roomUseCase.createRoom(room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
        return Response.ok().entity(result).build();
    }

    @DELETE
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN"})
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
