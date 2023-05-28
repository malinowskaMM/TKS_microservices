package p.lodz.tks.rent.service.application.core.domain.model.exceptions;

import javax.ws.rs.NotFoundException;

public class RentWithGivenIdNotFound extends NotFoundException {
    public RentWithGivenIdNotFound(String message) {
        super(message);
    }
}
