package p.lodz.tks.user.service.repositories.adapters.aggregates.user.converter;

import org.junit.jupiter.api.Test;
import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.repositories.modelEnt.user.AccessLevelEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.UserEnt;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryAdapterTest {

    @Test
    void convertUserToUserEnt() {
        User user = new User(true, "KTumulec", "password", AccessLevel.CLIENT);
        UserEnt userEnt = UserEntConverter.convertUserToUserEnt(user);
        assertEquals(user.getLogin(), userEnt.getLogin());
    }

    @Test
    void convertUserEntToUser() {
        UserEnt userEnt = new UserEnt(true, "KTumulec", "password", AccessLevelEnt.CLIENT);
        User user = UserEntConverter.convertUserEntToUser(userEnt);
        assertEquals(user.getLogin(), userEnt.getLogin());
    }
}