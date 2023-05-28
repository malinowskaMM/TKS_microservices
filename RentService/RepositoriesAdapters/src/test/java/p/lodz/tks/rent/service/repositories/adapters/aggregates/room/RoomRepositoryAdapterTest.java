package p.lodz.tks.rent.service.repositories.adapters.aggregates.room;

import org.junit.jupiter.api.Test;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;
import p.lodz.tks.rent.service.repositories.adapters.aggregates.room.converter.RoomEntConverter;
import p.lodz.tks.rent.service.repositories.modelEnt.room.RoomEnt;

import static org.junit.jupiter.api.Assertions.*;
class RoomRepositoryAdapterTest {
    @Test
    void convertRoomToRoomEnt() {
        Room room = new Room(1, 100.0, 2);
        RoomEnt roomEnt = RoomEntConverter.convertRoomToRoomEnt(room);

        assertEquals(room.getRoomCapacity(), roomEnt.getRoomCapacity());
        assertEquals(room.getRoomNumber(), roomEnt.getRoomNumber());
        assertEquals(room.getPrice(), roomEnt.getPrice());
    }

    @Test
    void convertRoomEntToRoom() {
        RoomEnt roomEnt = new RoomEnt(1, 100.0, 2);
        Room room = RoomEntConverter.convertRoomEntToRoom(roomEnt);

        assertEquals(room.getRoomCapacity(), roomEnt.getRoomCapacity());
        assertEquals(room.getRoomNumber(), roomEnt.getRoomNumber());
        assertEquals(room.getPrice(), roomEnt.getPrice());
    }
}