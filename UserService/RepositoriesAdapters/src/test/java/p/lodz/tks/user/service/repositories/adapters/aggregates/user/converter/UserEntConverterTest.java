package p.lodz.tks.user.service.repositories.adapters.aggregates.user.converter;

import org.junit.jupiter.api.Test;
import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.user.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.user.service.application.core.domain.model.model.user.manager.Manager;
import p.lodz.tks.user.service.repositories.modelEnt.user.AccessLevelEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.admin.AdminEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.client.ClientEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.manager.ManagerEnt;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryAdapterTest {

    @Test
    void convertUserToUserEnt() {
        Client client = new Client("123456789", "Kamil", "Tumulec", "Warszawa", "KTumulec", "password", AccessLevel.CLIENT);
        Admin admin = new Admin("admin", "password", AccessLevel.ADMIN);
        Manager manager = new Manager("manager", "password", AccessLevel.MANAGER);

        ClientEnt clientEnt = (ClientEnt) UserEntConverter.convertUserToUserEnt(client);
        AdminEnt adminEnt = (AdminEnt) UserEntConverter.convertUserToUserEnt(admin);
        ManagerEnt managerEnt = (ManagerEnt) UserEntConverter.convertUserToUserEnt(manager);

        assertEquals(client.getLastName(), clientEnt.getLastName());
        assertEquals(admin.getLogin(), adminEnt.getLogin());
        assertEquals(manager.getLogin(), managerEnt.getLogin());
    }

    @Test
    void convertUserEntToUser() {
        ClientEnt clientEnt = new ClientEnt("123456789", "Kamil", "Tumulec", "Warszawa", "KTumulec", "password", AccessLevelEnt.CLIENT);
        AdminEnt adminEnt = new AdminEnt("admin", "password", AccessLevelEnt.ADMIN);
        ManagerEnt managerEnt = new ManagerEnt("manager", "password", AccessLevelEnt.MANAGER);

        Client client = (Client) UserEntConverter.convertUserEntToUser(clientEnt);
        Admin admin = (Admin) UserEntConverter.convertUserEntToUser(adminEnt);
        Manager manager = (Manager) UserEntConverter.convertUserEntToUser(managerEnt);

        assertEquals(client.getLastName(), clientEnt.getLastName());
        assertEquals(admin.getLogin(), adminEnt.getLogin());
        assertEquals(manager.getLogin(), managerEnt.getLogin());
    }
}