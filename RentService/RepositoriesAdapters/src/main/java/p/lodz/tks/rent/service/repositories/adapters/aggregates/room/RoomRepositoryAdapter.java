package p.lodz.tks.rent.service.repositories.adapters.aggregates.room;


import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;
import p.lodz.tks.rent.service.application.ports.control.room.CreateRoomPort;
import p.lodz.tks.rent.service.application.ports.control.room.DeleteRoomPort;
import p.lodz.tks.rent.service.application.ports.control.room.ModifyRoomPort;
import p.lodz.tks.rent.service.application.ports.infrastructure.room.GetRoomPort;
import p.lodz.tks.rent.service.repositories.adapters.aggregates.room.converter.RoomEntConverter;
import p.lodz.tks.rent.service.repositories.adapters.repositories.RoomRepository;
import p.lodz.tks.rent.service.repositories.modelEnt.room.RoomEnt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@ApplicationScoped
public class RoomRepositoryAdapter implements CreateRoomPort, DeleteRoomPort, GetRoomPort, ModifyRoomPort {

    @Inject
    RoomRepository roomRepository;

    @Override
    public Room createRoom(Integer roomNumber, Double price, Integer roomCapacity) {
        RoomEnt roomEnt = roomRepository.createRoom(roomNumber, price, roomCapacity);
        return RoomEntConverter.convertRoomEntToRoom(roomEnt);
    }

    @Override
    public void deleteRoom(UUID id) {
        roomRepository.deleteRoom(id);
    }

    @Override
    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        for (RoomEnt roomEnt : roomRepository.getRooms()) {
            rooms.add(RoomEntConverter.convertRoomEntToRoom(roomEnt));
        }
        return rooms;
    }

    @Override
    public List<Room> getRoomsBy(Predicate<Room> predicate) {
        return getRooms().stream().filter(predicate).toList();
    }

    @Override
    public Room getRoomById(UUID id) {
        return RoomEntConverter.convertRoomEntToRoom(roomRepository.getRoomById(id));
    }

    @Override
    public Room modifyRoom(UUID id, Integer roomNumber, Double price, Integer roomCapacity) {
        return RoomEntConverter.convertRoomEntToRoom(roomRepository.modifyRoom(id, roomNumber, price, roomCapacity));
    }
}
