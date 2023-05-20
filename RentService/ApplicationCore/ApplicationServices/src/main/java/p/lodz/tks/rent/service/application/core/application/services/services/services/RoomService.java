package p.lodz.tks.rent.service.application.core.application.services.services.services;

import com.nimbusds.jose.JOSEException;
import org.json.simple.JSONObject;
import p.lodz.tks.rent.service.application.core.application.services.services.auth.JwsGenerator;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.RoomValidationFailed;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.RoomWithGivenIdNotFound;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;
import p.lodz.tks.rent.service.application.ports.control.room.CreateRoomPort;
import p.lodz.tks.rent.service.application.ports.control.room.DeleteRoomPort;
import p.lodz.tks.rent.service.application.ports.control.room.ModifyRoomPort;
import p.lodz.tks.rent.service.application.ports.infrastructure.rent.GetRentPort;
import p.lodz.tks.rent.service.application.ports.infrastructure.room.GetRoomPort;
import p.lodz.tks.rent.service.room.RoomUseCase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@ApplicationScoped
public class RoomService implements RoomUseCase {
    @Inject
    private CreateRoomPort createRoomPort;

    @Inject
    private DeleteRoomPort deleteRoomPort;

    @Inject
    private GetRoomPort getRoomPort;

    @Inject
    private ModifyRoomPort modifyRoomPort;

    @Inject
    private GetRentPort getRentPort;

    private final JwsGenerator jwsGenerator = new JwsGenerator();

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public synchronized Room createRoom(Integer roomNumber, Double basePrice, int roomCapacity)
            throws RoomValidationFailed {
        final Room room = new Room(roomNumber, basePrice, roomCapacity);
        if (validator.validate(room).isEmpty()) {
            return createRoomPort.createRoom(room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
        } else {
            throw new RoomValidationFailed("Cannot create room with given parameters");
        }
    }

    @Override
    public synchronized void removeRoom(UUID id) throws RoomWithGivenIdNotFound {
        final Room room1 = getRoomPort.getRoomById(id);
        if (room1 == null || !getRentPort.getRentsByRoom(id).isEmpty()) {
            throw new RoomWithGivenIdNotFound("Cannot delete room");
        } else {
            deleteRoomPort.deleteRoom(room1.getUuid());
        }

    }

    @Override
    public synchronized void updateRoom(UUID id, String jws, Integer roomNumber, Double basePrice, int roomCapacity) throws RoomWithGivenIdNotFound, ParseException, JOSEException {
        final Room room1 = getRoomPort.getRoomById(id);
        if (room1 == null) {
            throw new RoomWithGivenIdNotFound("Cannot update room");
        } else {
            if (this.jwsGenerator.verify(jws)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("uuid", room1.getUuid().toString());
                String newJwt = this.jwsGenerator.generateJws(jsonObject.toString());
                if(newJwt.equals(jws)) {
                    modifyRoomPort.modifyRoom(room1.getUuid(), roomNumber, basePrice, roomCapacity);
                }
            }
        }
    }

    @Override
    public Room getRoomById(UUID id) {
        final Room room = getRoomPort.getRoomById(id);
        if (room == null) {
            throw new NotFoundException("Cannot update room");
        }
        return room;
    }

    @Override
    public List<Room> getRooms(Predicate<Room> predicate) {
        return getRoomPort.getRoomsBy(predicate);
    }

    @Override
    public List<Room> getAllRooms() {
        return getRoomPort.getRooms();
    }

}
