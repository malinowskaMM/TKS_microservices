package p.lodz.tks.rent.service.application.core.domain.model.exceptions;

import jakarta.validation.ValidationException;

public class PasswordMatch extends ValidationException {
    public PasswordMatch(String message) {
        super(message);
    }
}
