package p.lodz.tks.rent.service.application.core.domain.model.exceptions;

import jakarta.validation.ValidationException;

public class AdminValidationFailed extends ValidationException {
    public AdminValidationFailed(String message) {
        super(message);
    }
}
