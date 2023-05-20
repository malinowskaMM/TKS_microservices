package p.lodz.tks.rent.service.room;

import com.nimbusds.jose.JOSEException;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface RoomUseCase {
    Room createRoom(Integer roomNumber, Double basePrice, int roomCapacity);
    void removeRoom(UUID id);
    void updateRoom(UUID id, String jws, Integer roomNumber, Double basePrice, int roomCapacity) throws ParseException, JOSEException;
    Room getRoomById(UUID id);
    List<Room> getRooms(Predicate<Room> predicate);
    List<Room> getAllRooms();
}
