package p.lodz.tks.user.service.application.core.domain.model.exceptions;

import javax.ws.rs.NotFoundException;

public class RoomWithGivenIdNotFound extends NotFoundException {
    public RoomWithGivenIdNotFound(String message) {
        super(message);
    }
}
