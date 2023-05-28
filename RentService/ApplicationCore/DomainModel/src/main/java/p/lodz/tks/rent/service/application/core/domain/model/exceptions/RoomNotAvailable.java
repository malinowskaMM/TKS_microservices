package p.lodz.tks.rent.service.application.core.domain.model.exceptions;

import javax.ws.rs.NotAllowedException;

public class RoomNotAvailable extends NotAllowedException {
    public RoomNotAvailable(String message) {
        super(message);
    }
}
