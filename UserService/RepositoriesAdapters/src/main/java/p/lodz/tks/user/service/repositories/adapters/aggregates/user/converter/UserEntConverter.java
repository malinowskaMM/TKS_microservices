package p.lodz.tks.user.service.repositories.adapters.aggregates.user.converter;

import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.user.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.user.service.application.core.domain.model.model.user.manager.Manager;
import p.lodz.tks.user.service.repositories.modelEnt.user.AccessLevelEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.UserEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.admin.AdminEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.client.ClientEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.manager.ManagerEnt;

public class UserEntConverter {

    public static UserEnt convertUserToUserEnt(User user) {
        if (user instanceof Admin) {
            return new AdminEnt(user.getUuid(), user.getLogin(), user.getPassword(), convertAccessLevelToAccessLevelEnt(user.getAccessLevel()));
        }
        else if (user instanceof Client) {
            return new ClientEnt(user.getUuid(), ((Client) user).getPersonalId(), ((Client) user).getFirstName(), ((Client) user).getLastName(), ((Client) user).getAddress(), user.getLogin(), user.getPassword(), convertAccessLevelToAccessLevelEnt(user.getAccessLevel()));
        }
        else {
            return new ManagerEnt(user.getUuid(), user.getLogin(), user.getPassword(), convertAccessLevelToAccessLevelEnt(user.getAccessLevel()));
        }
    }

    public static User convertUserEntToUser(UserEnt userEnt) {
        if(userEnt == null){
            return null;
        }
        if (userEnt instanceof AdminEnt) {
            return new Admin(userEnt.getUuid(), userEnt.getLogin(), userEnt.getPassword(), convertAccessLevelEntToAccessLevel(userEnt.getAccessLevel()));
        }
        else if (userEnt instanceof ClientEnt) {
            return new Client(userEnt.getUuid(), ((ClientEnt) userEnt).getPersonalId(), ((ClientEnt) userEnt).getFirstName(), ((ClientEnt) userEnt).getLastName(), ((ClientEnt) userEnt).getAddress(), userEnt.getLogin(), userEnt.getPassword(), convertAccessLevelEntToAccessLevel(userEnt.getAccessLevel()));
        }
        else {
            return new Manager(userEnt.getUuid(), userEnt.getLogin(), userEnt.getPassword(), convertAccessLevelEntToAccessLevel(userEnt.getAccessLevel()));
        }
    }

    public static AccessLevelEnt convertAccessLevelToAccessLevelEnt(AccessLevel accessLevel) {
        return switch (accessLevel.getAccessLevel()) {
            case "ADMIN" -> AccessLevelEnt.ADMIN;
            case "CLIENT" -> AccessLevelEnt.CLIENT;
            case "MANAGER" -> AccessLevelEnt.MANAGER;
            default -> AccessLevelEnt.NONE;
        };
    }

    public static AccessLevel convertAccessLevelEntToAccessLevel(AccessLevelEnt accessLevelEnt) {
        return switch (accessLevelEnt.getAccessLevel()) {
            case "ADMIN" -> AccessLevel.ADMIN;
            case "CLIENT" -> AccessLevel.CLIENT;
            case "MANAGER" -> AccessLevel.MANAGER;
            default -> AccessLevel.NONE;
        };
    }
}
