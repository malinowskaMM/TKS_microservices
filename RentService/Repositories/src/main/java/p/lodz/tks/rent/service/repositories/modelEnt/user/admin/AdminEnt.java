package p.lodz.tks.rent.service.repositories.modelEnt.user.admin;

import p.lodz.tks.rent.service.repositories.modelEnt.user.AccessLevelEnt;
import p.lodz.tks.rent.service.repositories.modelEnt.user.UserEnt;

import java.util.UUID;

public class AdminEnt extends UserEnt {

    public AdminEnt(String login, String password, AccessLevelEnt accessLevel) {
        super(true, login, password, accessLevel);
    }

    public AdminEnt(UUID uuid, String login, String password, AccessLevelEnt accessLevel) {
        super(uuid, true, login, password, accessLevel);
    }
}
