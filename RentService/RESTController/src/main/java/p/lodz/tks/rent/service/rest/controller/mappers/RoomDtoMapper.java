package p.lodz.tks.rent.service.rest.controller.mappers;

import lombok.NoArgsConstructor;
import p.lodz.tks.rent.service.application.core.domain.model.dto.room.RoomDto;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;

@NoArgsConstructor
public class RoomDtoMapper {
    public Room toRoom(RoomDto roomDto) {
        return new Room(roomDto.getRoomNumber(), roomDto.getPrice(), roomDto.getRoomCapacity());
    }
}
