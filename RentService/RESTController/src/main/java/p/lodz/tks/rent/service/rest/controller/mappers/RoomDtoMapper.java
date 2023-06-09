package p.lodz.tks.rent.service.rest.controller.mappers;

import jakarta.ejb.Stateless;
import lombok.NoArgsConstructor;
import p.lodz.tks.rent.service.rest.controller.dto.room.RoomDto;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;

@NoArgsConstructor
@Stateless
public class RoomDtoMapper {
    public Room toRoom(RoomDto roomDto) {
        return new Room(roomDto.getRoomNumber(), roomDto.getPrice(), roomDto.getRoomCapacity());
    }
}
