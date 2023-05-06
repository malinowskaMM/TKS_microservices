package p.lodz.tks.user.service.application.core.domain.model.model.user.manager;



import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;

import java.util.UUID;

public class Manager extends User {

    public Manager(String login, String password, AccessLevel accessLevel) {
        super(true, login, password, accessLevel);
    }

    public Manager(UUID uuid, String login, String password, AccessLevel accessLevel) {
        super(uuid, true, login, password, accessLevel);
    }

    public Manager() {
    }
}
