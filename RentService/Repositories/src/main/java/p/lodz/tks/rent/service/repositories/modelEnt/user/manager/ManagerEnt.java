package p.lodz.tks.rent.service.repositories.modelEnt.user.manager;


import p.lodz.tks.rent.service.repositories.modelEnt.user.AccessLevelEnt;
import p.lodz.tks.rent.service.repositories.modelEnt.user.UserEnt;

import java.util.UUID;

public class ManagerEnt extends UserEnt {

    public ManagerEnt(String login, String password, AccessLevelEnt accessLevel) {
        super(true, login, password, accessLevel);
    }
    public ManagerEnt(UUID uuid, String login, String password, AccessLevelEnt accessLevel) {
        super(uuid, true, login, password, accessLevel);
    }
}
