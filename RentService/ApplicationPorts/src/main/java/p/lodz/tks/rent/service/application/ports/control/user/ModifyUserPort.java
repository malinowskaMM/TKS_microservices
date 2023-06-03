package p.lodz.tks.rent.service.application.ports.control.user;


import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;

import java.util.UUID;

public interface ModifyUserPort {

    User modifyClient(UUID id, String login, String password, AccessLevel accessLevel , String firstName, String lastName, String address);
    void activateUser(UUID id);
    void deactivateUser(UUID id);
}