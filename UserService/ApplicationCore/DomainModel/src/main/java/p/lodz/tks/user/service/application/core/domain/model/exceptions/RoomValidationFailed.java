package p.lodz.tks.user.service.application.core.domain.model.exceptions;

import javax.validation.ValidationException;

public class RoomValidationFailed extends ValidationException {
    public RoomValidationFailed(String message) {
        super(message);
    }
}