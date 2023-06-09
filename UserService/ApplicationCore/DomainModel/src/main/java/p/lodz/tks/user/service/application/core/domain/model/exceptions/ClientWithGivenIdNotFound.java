package p.lodz.tks.user.service.application.core.domain.model.exceptions;

import jakarta.ws.rs.NotFoundException;

public class ClientWithGivenIdNotFound extends NotFoundException {
    public ClientWithGivenIdNotFound(String message) {
        super(message);
    }
}
