package p.lodz.tks.user.service.application.core.domain.model.exceptions;

import jakarta.validation.ValidationException;

public class AdminValidationFailed extends ValidationException {
    public AdminValidationFailed(String message) {
        super(message);
    }
}
