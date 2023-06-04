package p.lodz.tks.rent.service.application.ports.control.user;


import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.manager.Manager;

import java.util.UUID;

public interface CreateUserPort {

    Client createClient(UUID uuid, String personalId, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel);
    Admin createAdmin(UUID uuid,String login, String password, AccessLevel accessLevel);
    Manager createManager(UUID uuid, String login, String password, AccessLevel accessLevel);
    boolean isLoginUnique(String login);
}