package p.lodz.tks.rent.service.repositories.adapters.repositories;

import p.lodz.tks.rent.service.repositories.modelEnt.user.AccessLevelEnt;
import p.lodz.tks.rent.service.repositories.modelEnt.user.UserEnt;
import p.lodz.tks.rent.service.repositories.modelEnt.user.admin.AdminEnt;
import p.lodz.tks.rent.service.repositories.modelEnt.user.client.ClientEnt;
import p.lodz.tks.rent.service.repositories.modelEnt.user.manager.ManagerEnt;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface UserRepository {

    ClientEnt createClient(String personalId, String firstName, String lastName, String address, String login, String password, AccessLevelEnt accessLevel);
    AdminEnt createAdmin(String login, String password, AccessLevelEnt accessLevel);
    ManagerEnt createManager(String login, String password, AccessLevelEnt accessLevel);
    List<UserEnt> getUsers();
    List<UserEnt> getUsersBy(Predicate<UserEnt> predicate);
    UserEnt modifyUser(UUID id, String login, String password, AccessLevelEnt accessLevel , String firstName, String lastName, String address);
    UserEnt getUserById(UUID id);
    void activateUser(UUID id);
    void deactivateUser(UUID id);
    UserEnt findUserByLogin(String login);
    void deleteUser(UUID id);
    boolean isLoginUnique(String login);
}
