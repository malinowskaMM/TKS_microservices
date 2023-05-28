package p.lodz.tks.rent.service.application.core.domain.model.exceptions;

import javax.validation.ValidationException;

public class PasswordMatchFailed extends ValidationException {
    public PasswordMatchFailed(String message) {
        super(message);
    }
}
