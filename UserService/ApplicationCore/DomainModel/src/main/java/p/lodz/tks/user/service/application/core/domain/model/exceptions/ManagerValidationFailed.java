package p.lodz.tks.user.service.application.core.domain.model.exceptions;

import javax.validation.ValidationException;

public class ManagerValidationFailed extends ValidationException {
    public ManagerValidationFailed(String message) {
        super(message);
    }
}
