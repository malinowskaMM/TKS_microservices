package p.lodz.tks.rent.service.application.ports.infrastructure.user;

import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface GetUserPort {

    User getUserById(UUID id);
    List<User> getUsers();
    List<User> getUsersBy(Predicate<User> predicate);
    User findUserByLogin(String login);
}