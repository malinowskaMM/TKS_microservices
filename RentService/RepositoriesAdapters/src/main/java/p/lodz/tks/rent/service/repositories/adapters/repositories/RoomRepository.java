package p.lodz.tks.rent.service.repositories.adapters.repositories;


import p.lodz.tks.rent.service.repositories.modelEnt.room.RoomEnt;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface RoomRepository {

    RoomEnt createRoom(Integer roomNumber, Double price, Integer roomCapacity);
    List<RoomEnt> getRooms();
    List<RoomEnt> getRoomsBy(Predicate<RoomEnt> predicate);
    RoomEnt modifyRoom(UUID id, Integer roomNumber, Double price, Integer roomCapacity);
    void deleteRoom(UUID id);
    RoomEnt getRoomById(UUID id);
}
