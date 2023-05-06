package p.lodz.tks.user.service.application.core.domain.model.exceptions;

import javax.validation.ValidationException;

public class ClientValidationFailed extends ValidationException {
    public ClientValidationFailed(String message) {
        super(message);
    }
}
