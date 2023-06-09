package p.lodz.tks.user.service.application.core.application.services.auth;

import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.application.ports.infrastructure.user.GetUserPort;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import java.util.*;

@ApplicationScoped
public class AuthIdentityStore implements IdentityStore {

    @Inject
    private GetUserPort getUserPort;

    @Override
    public int priority() {
        return 70;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(ValidationType.VALIDATE, ValidationType.PROVIDE_GROUPS);
    }

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        User user = new ArrayList<>(getUserPort.getUsers().stream().filter(user1 -> user1.getLogin().equals(
                validationResult.getCallerPrincipal().getName())).toList()).get(0);
        return new HashSet<>(Collections.singleton(user.getAccessLevel().toString()));
    }

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
            User user = getUserPort.findUserByLogin(credential.getCaller() /*loogin*/,
                    credential.getPasswordAsString() /*pass*/);
            if (user != null && user.isActive()) {
                return new CredentialValidationResult(user.getLogin(), /*role*/new HashSet<>(Collections.singleton(user.getAccessLevel().toString())));
            }
        return CredentialValidationResult.INVALID_RESULT;
    }
}
