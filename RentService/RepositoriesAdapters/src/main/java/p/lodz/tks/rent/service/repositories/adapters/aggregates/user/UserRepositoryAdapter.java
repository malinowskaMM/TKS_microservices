package p.lodz.tks.rent.service.repositories.adapters.aggregates.user;



import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.manager.Manager;
import p.lodz.tks.rent.service.application.ports.control.user.CreateUserPort;
import p.lodz.tks.rent.service.application.ports.control.user.DeleteUserPort;
import p.lodz.tks.rent.service.application.ports.control.user.ModifyUserPort;
import p.lodz.tks.rent.service.application.ports.infrastructure.user.GetUserPort;
import p.lodz.tks.rent.service.repositories.adapters.aggregates.user.converter.UserEntConverter;
import p.lodz.tks.rent.service.repositories.adapters.repositories.UserRepository;
import p.lodz.tks.rent.service.repositories.modelEnt.user.UserEnt;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@ApplicationScoped
public class UserRepositoryAdapter implements CreateUserPort, DeleteUserPort, GetUserPort, ModifyUserPort {

    @Inject
    UserRepository userRepository;

    @Override
    public Client createClient(String personalId, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel) {
        return (Client) UserEntConverter.convertUserEntToUser(userRepository.createClient(personalId, firstName, lastName, address, login, password, UserEntConverter.convertAccessLevelToAccessLevelEnt(accessLevel)));
    }

    @Override
    public Admin createAdmin(String login, String password, AccessLevel accessLevel) {
        return (Admin) UserEntConverter.convertUserEntToUser(userRepository.createAdmin(login, password, UserEntConverter.convertAccessLevelToAccessLevelEnt(accessLevel)));
    }

    @Override
    public Manager createManager(String login, String password, AccessLevel accessLevel) {
        return (Manager) UserEntConverter.convertUserEntToUser(userRepository.createManager(login, password, UserEntConverter.convertAccessLevelToAccessLevelEnt(accessLevel)));
    }

    @Override
    public boolean isLoginUnique(String login) {
        return userRepository.isLoginUnique(login);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteUser(id);
    }

    @Override
    public User getUserById(UUID id) {
        return UserEntConverter.convertUserEntToUser(userRepository.getUserById(id));
    }

    @Override
    public List<User> getUsers() {
        List<UserEnt> usersEnt = userRepository.getUsers();
        List<User> users = new ArrayList<>();

        for (UserEnt userEnt : usersEnt) {
            users.add(UserEntConverter.convertUserEntToUser(userEnt));
        }

        return users;
    }

    @Override
    public List<User> getUsersBy(Predicate<User> predicate) {
        return getUsers().stream().filter(predicate).toList();
    }

    @Override
    public User findUserByLogin(String login, String password) {
        return UserEntConverter.convertUserEntToUser(userRepository.findUserByLogin(login, password));
    }

    @Override
    public User modifyUser(UUID id, String login, String password, AccessLevel accessLevel, String firstName, String lastName, String address) {
        return UserEntConverter.convertUserEntToUser(userRepository.modifyUser(id, login, password, UserEntConverter.convertAccessLevelToAccessLevelEnt(accessLevel), firstName, lastName, address));
    }

    @Override
    public void activateUser(UUID id) {
        userRepository.activateUser(id);
    }

    @Override
    public void deactivateUser(UUID id) {
        userRepository.deactivateUser(id);
    }
}
