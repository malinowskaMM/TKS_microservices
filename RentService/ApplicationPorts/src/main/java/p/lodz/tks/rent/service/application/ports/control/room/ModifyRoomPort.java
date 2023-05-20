package p.lodz.tks.rent.service.application.ports.control.room;


import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;

import java.util.UUID;

public interface ModifyRoomPort {

    Room modifyRoom(UUID id, Integer roomNumber, Double price, Integer roomCapacity);
}