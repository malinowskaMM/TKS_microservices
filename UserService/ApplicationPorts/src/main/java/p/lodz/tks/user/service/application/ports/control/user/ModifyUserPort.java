package p.lodz.tks.user.service.application.ports.control.user;


import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;

import java.util.UUID;

public interface ModifyUserPort {

    User modifyUser(UUID id, String login, String password, AccessLevel accessLevel , String firstName, String lastName, String address);
    void activateUser(UUID id);
    void deactivateUser(UUID id);
}