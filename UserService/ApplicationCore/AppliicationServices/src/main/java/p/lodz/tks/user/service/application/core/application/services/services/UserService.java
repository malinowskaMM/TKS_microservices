package p.lodz.tks.user.service.application.core.application.services.services;

import p.lodz.tks.user.service.application.core.domain.model.exceptions.PasswordMatch;
import p.lodz.tks.user.service.application.core.domain.model.exceptions.UserWithGivenIdNotFound;
import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.application.ports.control.user.CreateUserPort;
import p.lodz.tks.user.service.application.ports.control.user.DeleteUserPort;
import p.lodz.tks.user.service.application.ports.control.user.ModifyUserPort;
import p.lodz.tks.user.service.application.ports.infrastructure.user.GetUserPort;
import p.lodz.tks.user.service.dto.user.ShowUserDto;
import p.lodz.tks.user.service.dto.user.mapper.UserDtoMapper;
import p.lodz.tks.user.service.user.UserUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@ApplicationScoped
public class UserService implements UserUseCase {

    @Inject
    private CreateUserPort createUserPort;

    @Inject
    private GetUserPort getUserPort;

    @Inject
    private ModifyUserPort modifyUserPort;

    @Inject
    private DeleteUserPort deleteUserPort;

    @Context
    private SecurityContext securityContext;

    @Override
    public synchronized void updateUser(UUID id, String login, String password, AccessLevel accessLevel) throws UserWithGivenIdNotFound {
        final User user = getUserPort.getUserById(id);
        if (user == null) {
            throw new UserWithGivenIdNotFound("Not found user with given id");
        } else {
            modifyUserPort.modifyUser(id, login, password, accessLevel);
        }
    }

    @Override
    public void addUser(User user) {
        createUserPort.addUser(user);
    }

    @Override
    public ShowUserDto getUserById(UUID id) throws UserWithGivenIdNotFound {
        final User user = getUserPort.getUserById(id);
        if (user == null) {
            throw new UserWithGivenIdNotFound("Not found user with given id");
        }
        return UserDtoMapper.toShowUserDto(user);
    }

    @Override
    public User getUserByIdInside(UUID id) throws UserWithGivenIdNotFound {
        final User user = getUserPort.getUserById(id);
        if (user == null) {
            throw new UserWithGivenIdNotFound("Not found user with given id");
        }
        return user;
    }

    @Override
    public List<ShowUserDto> findClientsByLoginPart(String login) {
        return findClients(user -> user.getLogin().contains(login));
    }

    @Override
    public ShowUserDto findUserByLogin(String login, String password) {
        return UserDtoMapper.toShowUserDto(getUserPort.findUserByLogin(login, password));
    }

    @Override
    public List<ShowUserDto> findClients(Predicate<User> predicate) {
        return getUserPort.getUsersBy(predicate).stream().map(UserDtoMapper::toShowUserDto).toList();
    }

    @Override
    public void activateUser(UUID id, String jws) throws UserWithGivenIdNotFound {
        modifyUserPort.activateUser(getUserByIdInside(id).getUuid());
    }

    @Override
    public void deactivateUser(UUID id, String jws) throws UserWithGivenIdNotFound {
        modifyUserPort.deactivateUser(getUserByIdInside(id).getUuid());
    }

    @Override
    public List<ShowUserDto> getAllUsers(){
        return getUserPort.getUsers().stream().map(UserDtoMapper::toShowUserDto).toList();
    }

    @Override
    public List<User> getAllUsersInside(){
        return getUserPort.getUsers();
    }

    @Override
    public void deleteUser(UUID id) throws UserWithGivenIdNotFound {
        deleteUserPort.deleteUser(getUserByIdInside(id).getUuid());
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        User user = getAllUsersInside().stream().filter(user1 -> user1.getLogin().equals(securityContext.getUserPrincipal().getName())).findFirst().orElse(null);
        if(user != null && !oldPassword.equals(newPassword)) {
            user.setPassword(newPassword);
            return true;
        }
        if(oldPassword.equals(newPassword)) {
            throw new PasswordMatch("New password and old password matches");
        }
        return false;
    }

    public User getUserFromServerContext() {
        return this.getAllUsersInside().stream().filter(user -> user.getLogin().equals(securityContext.getUserPrincipal().getName())).toList().get(0);
    }

}
