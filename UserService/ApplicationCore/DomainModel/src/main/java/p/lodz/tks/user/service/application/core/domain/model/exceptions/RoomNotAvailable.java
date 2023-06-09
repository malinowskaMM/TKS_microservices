package p.lodz.tks.user.service.application.core.domain.model.exceptions;

import jakarta.ws.rs.NotAllowedException;

public class RoomNotAvailable extends NotAllowedException {
    public RoomNotAvailable(String message) {
        super(message);
    }
}
