package p.lodz.tks.rent.service.application.ports.control.user;

import java.util.UUID;

public interface DeleteUserPort {

    void deleteUser(UUID id);
    void deleteUserByLogin(String login);
}
