package p.lodz.tks.rent.service.application.core.domain.model.dto.room.mapper;

import lombok.NoArgsConstructor;
import p.lodz.tks.rent.service.application.core.domain.model.dto.room.RoomDto;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;

import javax.ejb.Stateless;


@NoArgsConstructor
@Stateless
public class RoomDtoMapper {

    public Room toRoom(RoomDto roomDto) {
        return new Room(roomDto.getRoomNumber(), roomDto.getPrice(), roomDto.getRoomCapacity());
    }

}
