package p.lodz.tks.user.service.application.core.domain.model.exceptions;

import javax.ws.rs.NotFoundException;

public class RentWithGivenIdNotFound extends NotFoundException {
    public RentWithGivenIdNotFound(String message) {
        super(message);
    }
}
