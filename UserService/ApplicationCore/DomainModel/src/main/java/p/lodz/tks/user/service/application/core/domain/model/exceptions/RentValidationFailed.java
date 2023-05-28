package p.lodz.tks.user.service.application.core.domain.model.exceptions;

import javax.validation.ValidationException;

public class RentValidationFailed extends ValidationException {
    public RentValidationFailed(String message) {
        super(message);
    }
}
