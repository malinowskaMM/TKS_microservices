package p.lodz.tks.rent.service.application.core.domain.model.model.user.admin;



import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;

import java.util.UUID;

public class Admin extends User {

    public Admin(String login, String password, AccessLevel accessLevel) {
        super(true, login, password, accessLevel);
    }

    public Admin(UUID uuid, String login, String password, AccessLevel accessLevel) {
        super(uuid, true, login, password, accessLevel);
    }

    public Admin() {
    }
}
