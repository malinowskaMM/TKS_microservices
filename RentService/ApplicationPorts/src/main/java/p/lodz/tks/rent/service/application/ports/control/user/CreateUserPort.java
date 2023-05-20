package p.lodz.tks.rent.service.application.ports.control.user;


import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.manager.Manager;

public interface CreateUserPort {

    Client createClient(String personalId, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel);
    Admin createAdmin(String login, String password, AccessLevel accessLevel);
    Manager createManager(String login, String password, AccessLevel accessLevel);
    boolean isLoginUnique(String login);
}