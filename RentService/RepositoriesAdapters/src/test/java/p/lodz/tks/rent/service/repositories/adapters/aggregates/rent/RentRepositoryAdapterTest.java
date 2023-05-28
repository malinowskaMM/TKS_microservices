package p.lodz.tks.rent.service.repositories.adapters.aggregates.rent;

import org.junit.jupiter.api.Test;
import p.lodz.tks.rent.service.application.core.domain.model.model.rent.Rent;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.rent.service.repositories.adapters.aggregates.rent.converter.RentEntConverter;
import p.lodz.tks.rent.service.repositories.modelEnt.rent.RentEnt;
import p.lodz.tks.rent.service.repositories.modelEnt.room.RoomEnt;
import p.lodz.tks.rent.service.repositories.modelEnt.user.AccessLevelEnt;
import p.lodz.tks.rent.service.repositories.modelEnt.user.client.ClientEnt;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RentRepositoryAdapterTest {

    @Test
    void convertRentToRentEnt() {
        Room room = new Room(1, 100.0, 2);
        Client client = new Client("123456789", "Kamil", "Tumulec", "Warszawa", "KTumulec", "password", AccessLevel.CLIENT);
        Rent rent = new Rent(LocalDateTime.now(), LocalDateTime.MAX, client.getLogin(), room.getUuid().toString());
        RentEnt rentEnt = RentEntConverter.convertRentToRentEnt(rent);

        assertEquals(rent.getBeginTime(), rentEnt.getBeginTime());
        assertEquals(rent.getLogin(), rentEnt.getLogin());
        assertEquals(rent.getRoomId(), rentEnt.getRoomId());
        assertEquals(rent.getEndTime(), rentEnt.getEndTime());
    }

    @Test
    void convertRentEntToRent() {
        RoomEnt roomEnt = new RoomEnt(1, 100.0, 2);
        ClientEnt clientEnt = new ClientEnt("123456789", "Kamil", "Tumulec", "Warszawa", "KTumulec", "password", AccessLevelEnt.CLIENT);
        RentEnt rentEnt = new RentEnt(LocalDateTime.now(), LocalDateTime.MAX, clientEnt.getLogin(), roomEnt.getUuid().toString());
        Rent rent = RentEntConverter.convertRentEntToRent(rentEnt);

        assertEquals(rent.getBeginTime(), rentEnt.getBeginTime());
        assertEquals(rent.getLogin(), rentEnt.getLogin());
        assertEquals(rent.getRoomId(), rentEnt.getRoomId());
        assertEquals(rent.getEndTime(), rentEnt.getEndTime());
    }
}