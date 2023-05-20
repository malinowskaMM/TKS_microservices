package p.lodz.tks.rent.service.repositories.adapters.aggregates.room.converter;

import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;
import p.lodz.tks.rent.service.repositories.modelEnt.room.RoomEnt;

public class RoomEntConverter {

    public static RoomEnt convertRoomToRoomEnt(Room room) {
        return new RoomEnt(room.getUuid(), room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
    }

    public static Room convertRoomEntToRoom(RoomEnt roomEnt) {
        if(roomEnt == null) {
            return null;
        }
        return new Room(roomEnt.getUuid(), roomEnt.getRoomNumber(), roomEnt.getPrice(), roomEnt.getRoomCapacity());
    }
}
