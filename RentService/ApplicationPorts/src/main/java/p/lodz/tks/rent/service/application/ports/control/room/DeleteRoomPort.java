package p.lodz.tks.rent.service.application.ports.control.room;

import java.util.UUID;

public interface DeleteRoomPort {

    void deleteRoom(UUID id);
}