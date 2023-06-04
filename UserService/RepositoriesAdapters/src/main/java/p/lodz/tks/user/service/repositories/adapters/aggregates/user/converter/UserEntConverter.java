package p.lodz.tks.user.service.repositories.adapters.aggregates.user.converter;

import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.repositories.modelEnt.user.AccessLevelEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.UserEnt;

public class UserEntConverter {

    public static UserEnt convertUserToUserEnt(User user) {
        return new UserEnt(user.getUuid(), true, user.getLogin(), user.getPassword(), AccessLevelEnt.valueOf(user.getAccessLevel().getAccessLevel()));
    }

    public static User convertUserEntToUser(UserEnt userEnt) {
       return new User(
               userEnt.getUuid(),
               true,
               userEnt.getLogin(),
               userEnt.getPassword(),
               AccessLevel.valueOf(userEnt.getAccessLevel().getAccessLevel())
       );
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
