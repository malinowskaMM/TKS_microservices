package p.lodz.tks.rent.service.repositories.adapters.repositoriesImplementation;


import p.lodz.tks.rent.service.repositories.modelEnt.room.RoomEnt;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.Collections.synchronizedList;

@ApplicationScoped
public class RoomRepository implements p.lodz.tks.rent.service.repositories.adapters.repositories.RoomRepository {

    private final List<RoomEnt> rooms = synchronizedList(new ArrayList<>());

    @Override
    public RoomEnt createRoom(Integer roomNumber, Double price, Integer roomCapacity) {
        RoomEnt room = new RoomEnt(roomNumber, price, roomCapacity);
        rooms.add(room);
        return room;
    }

    @Override
    public List<RoomEnt> getRooms() {
        return rooms.stream().toList();
    }

    @Override
    public List<RoomEnt> getRoomsBy(Predicate<RoomEnt> predicate) {
        return getRooms().stream().filter(predicate).toList();
    }

    @Override
    public RoomEnt modifyRoom(UUID id, Integer roomNumber, Double price, Integer roomCapacity) {
        RoomEnt modifiedRoom = getRoomById(id);
        if(modifiedRoom != null) {
            modifiedRoom.setRoomNumber(roomNumber);
            modifiedRoom.setPrice(price);
            modifiedRoom.setRoomCapacity(roomCapacity);
            return modifiedRoom;
        }
        return null;
    }

    @Override
    public void deleteRoom(UUID id) {
        RoomEnt removed = getRoomById(id);
        if(removed != null) {
            rooms.remove(removed);
        }
    }

    @Override
    public RoomEnt getRoomById(UUID id) {
        Optional<RoomEnt> roomOptional = rooms.stream().filter(room -> room.getUuid().equals(id)).findFirst();
        return roomOptional.orElse(null);
    }
}
