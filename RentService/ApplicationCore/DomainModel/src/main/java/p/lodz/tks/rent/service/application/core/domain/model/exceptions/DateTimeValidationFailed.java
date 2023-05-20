package p.lodz.tks.rent.service.application.core.domain.model.exceptions;

import javax.validation.ValidationException;

public class DateTimeValidationFailed extends ValidationException {
    public DateTimeValidationFailed(String message) {
        super(message);
    }
}
