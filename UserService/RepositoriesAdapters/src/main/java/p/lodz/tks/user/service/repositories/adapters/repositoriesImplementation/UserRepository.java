package p.lodz.tks.user.service.repositories.adapters.repositoriesImplementation;


import p.lodz.tks.user.service.repositories.modelEnt.user.AccessLevelEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.UserEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.admin.AdminEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.client.ClientEnt;
import p.lodz.tks.user.service.repositories.modelEnt.user.manager.ManagerEnt;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.Collections.synchronizedList;

@ApplicationScoped
public class UserRepository implements p.lodz.tks.user.service.repositories.adapters.repositories.UserRepository {

    private final List<UserEnt> users = synchronizedList(new ArrayList<>());

    @Override
    public boolean isLoginUnique(String login) {
        return users.stream().filter(user -> user.getLogin().equals(login)).toList().isEmpty();
    }

    @Override
    public ClientEnt createClient(String personalId, String firstName, String lastName, String address, String login, String password, AccessLevelEnt accessLevel) {
        if(isLoginUnique(login)) {
            ClientEnt client = new ClientEnt(personalId, firstName, lastName, address, login, password, accessLevel);
            users.add(client);
            return client;
        }
        return null;
    }

    @Override
    public AdminEnt createAdmin(String login, String password, AccessLevelEnt accessLevel) {
        if(isLoginUnique(login)) {
            AdminEnt admin = new AdminEnt(login, password, accessLevel);
            users.add(admin);
            return admin;
        }
        return null;
    }

    @Override
    public ManagerEnt createManager(String login, String password, AccessLevelEnt accessLevel) {
        if(isLoginUnique(login)) {
            ManagerEnt manager = new ManagerEnt(login, password, accessLevel);
            users.add(manager);
            return manager;
        }
        return null;
    }

    @Override
    public List<UserEnt> getUsers() {
        return users.stream().toList();
    }

    @Override
    public List<UserEnt> getUsersBy(Predicate<UserEnt> predicate) {
        return getUsers().stream().filter(predicate).toList();
    }

    @Override
    public UserEnt modifyUser(UUID id, String login, String password, AccessLevelEnt accessLevel) {
        UserEnt user = getUserById(id);
        if(user != null) {
            user.setLogin(login);
            user.setPassword(password);
            user.setAccessLevel(accessLevel);
        }
        return null;
    }

    public UserEnt findUserByLogin(String login) {
        return getUsers().stream().filter(user -> (user.getLogin().equals(login))).toList().get(0);
    }

    @Override
    public UserEnt getUserById(UUID id) {
        Optional<UserEnt> userOptional = users.stream().filter(user -> user.getUuid().equals(id)).findFirst();
        return userOptional.orElse(null);
    }

    @Override
    public void activateUser(UUID id) {
        UserEnt user = getUserById(id);
        if(user != null) {
            user.activate();
        }
    }

    @Override
    public void deactivateUser(UUID id) {
        UserEnt user = getUserById(id);
        if(user != null) {
            user.deactivate();
        }
    }

    public void deleteUser(UUID id) {
        users.remove(getUserById(id));
    }

    public void addUser(UserEnt user) {
        // if (isLoginUnique(user.getLogin())) {
            users.add(user);
        // }
    }
}
