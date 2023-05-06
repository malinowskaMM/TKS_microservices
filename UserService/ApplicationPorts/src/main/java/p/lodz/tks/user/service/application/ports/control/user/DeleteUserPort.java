package p.lodz.tks.user.service.application.ports.control.user;

import java.util.UUID;

public interface DeleteUserPort {

    void deleteUser(UUID id);
}
