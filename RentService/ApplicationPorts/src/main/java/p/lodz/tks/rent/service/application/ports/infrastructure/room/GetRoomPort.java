package p.lodz.tks.rent.service.application.ports.infrastructure.room;

import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface GetRoomPort {

    List<Room> getRooms();
    List<Room> getRoomsBy(Predicate<Room> predicate);
    Room getRoomById(UUID id);
}