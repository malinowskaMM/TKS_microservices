package p.lodz.tks.user.service.application.core.domain.model.exceptions;

import javax.validation.ValidationException;

public class PasswordMatch extends ValidationException {
    public PasswordMatch(String message) {
        super(message);
    }
}