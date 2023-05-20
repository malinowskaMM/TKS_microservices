package p.lodz.tks.rent.service.application.ports.control.room;


import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;

public interface CreateRoomPort {

    Room createRoom(Integer roomNumber, Double price, Integer roomCapacity);
}