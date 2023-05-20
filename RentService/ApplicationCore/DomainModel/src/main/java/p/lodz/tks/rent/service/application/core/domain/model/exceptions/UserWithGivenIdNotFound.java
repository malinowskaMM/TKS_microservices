package p.lodz.tks.rent.service.application.core.domain.model.exceptions;

import javax.ws.rs.NotFoundException;

public class UserWithGivenIdNotFound extends NotFoundException {
    public UserWithGivenIdNotFound(String message) {
        super(message);
    }
}
