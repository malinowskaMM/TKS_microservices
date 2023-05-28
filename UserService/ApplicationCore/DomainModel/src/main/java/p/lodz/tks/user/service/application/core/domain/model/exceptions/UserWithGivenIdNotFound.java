package p.lodz.tks.user.service.application.core.domain.model.exceptions;

import javax.ws.rs.NotFoundException;

public class UserWithGivenIdNotFound extends NotFoundException {
    public UserWithGivenIdNotFound(String message) {
        super(message);
    }
}
