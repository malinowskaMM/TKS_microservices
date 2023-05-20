package p.lodz.tks.rent.service.application.core.domain.model.exceptions;

import jakarta.validation.ValidationException;

public class ManagerValidationFailed extends ValidationException {
    public ManagerValidationFailed(String message) {
        super(message);
    }
}
