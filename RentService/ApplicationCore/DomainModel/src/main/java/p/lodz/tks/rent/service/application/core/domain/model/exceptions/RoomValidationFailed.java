package p.lodz.tks.rent.service.application.core.domain.model.exceptions;

import jakarta.validation.ValidationException;

public class RoomValidationFailed extends ValidationException {
    public RoomValidationFailed(String message) {
        super(message);
    }
}
