package p.lodz.tks.rent.service.application.core.application.services.services.services;

import com.nimbusds.jose.JOSEException;
import org.json.simple.JSONObject;
import p.lodz.tks.rent.service.application.core.application.services.services.auth.JwsGenerator;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.*;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.manager.Manager;
import p.lodz.tks.rent.service.application.ports.control.user.CreateUserPort;
import p.lodz.tks.rent.service.application.ports.control.user.DeleteUserPort;
import p.lodz.tks.rent.service.application.ports.control.user.ModifyUserPort;
import p.lodz.tks.rent.service.application.ports.infrastructure.user.GetUserPort;
import p.lodz.tks.rent.service.dto.user.ShowUserDto;
import p.lodz.tks.rent.service.dto.user.mapper.UserDtoMapper;
import p.lodz.tks.rent.service.user.UserUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.text.ParseException;
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

    private final JwsGenerator jwsGenerator = new JwsGenerator();

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public synchronized Client registerClient(String firstName, String lastName, String personalId, String address, String login, String password) throws ClientValidationFailed {
        final Client client = new Client(personalId, firstName, lastName, address, login, password, AccessLevel.CLIENT);
        if (validator.validate(client).isEmpty()) {
                    return createUserPort.createClient(client.getPersonalId(), client.getFirstName(), client.getLastName(), client.getAddress(), client.getLogin(), client.getPassword(), client.getAccessLevel());
        } else {
            throw new ClientValidationFailed("Cannot register client");
        }
    }

    @Override
    public synchronized Manager registerManager(String login, String password) throws ManagerValidationFailed {
        final Manager manager = new Manager(login, password, AccessLevel.MANAGER);
        if (validator.validate(manager).isEmpty()) {
                return createUserPort.createManager(manager.getLogin(), manager.getPassword(), manager.getAccessLevel());
        } else {
            throw new ManagerValidationFailed("Cannot register manager");
        }
    }

    @Override
    public synchronized Admin registerAdmin(String login, String password) throws AdminValidationFailed {
        final Admin admin = new Admin(login, password, AccessLevel.ADMIN);
        if (validator.validate(admin).isEmpty()) {
                return createUserPort.createAdmin(admin.getLogin(), admin.getPassword(), admin.getAccessLevel());
        } else {
            throw new AdminValidationFailed("Cannot register admin");
        }
    }

    @Override
    public synchronized void updateUser(UUID id, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        final User user = getUserPort.getUserById(id);
        if (user == null) {
            throw new UserWithGivenIdNotFound("Not found user with given id");
        } else {
            modifyUserPort.modifyClient(id, login, password, accessLevel ,firstName, lastName, address);
        }
    }

    @Override
    public Client getClientById(UUID uuid) throws ClientWithGivenIdNotFound {
            final Client client = (Client) getUserPort.getUserById(uuid);
            if (client == null) {
                throw new ClientWithGivenIdNotFound("Not found client with given id");
            }
            return client;
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
    public ShowUserDto findUserByLogin(String login) {
        return UserDtoMapper.toShowUserDto(getUserPort.findUserByLogin(login));
    }

    @Override
    public List<ShowUserDto> findClients(Predicate<User> predicate) {
        return getUserPort.getUsersBy(predicate).stream().map(UserDtoMapper::toShowUserDto).toList();
    }

    @Override
    public void activateUser(UUID id, String jws) throws UserWithGivenIdNotFound, JOSEException, ParseException {
        if (this.jwsGenerator.verify(jws)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uuid", getUserByIdInside(id).getUuid().toString());
            String newJwt = this.jwsGenerator.generateJws(jsonObject.toString());
            if(newJwt.equals(jws)) {
                modifyUserPort.activateUser(getUserByIdInside(id).getUuid());
            }
        }
    }

    @Override
    public void deactivateUser(UUID id, String jws) throws UserWithGivenIdNotFound, JOSEException, ParseException {
        if (this.jwsGenerator.verify(jws)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uuid", getUserByIdInside(id).getUuid().toString());
            String newJwt = this.jwsGenerator.generateJws(jsonObject.toString());
            if(newJwt.equals(jws)) {
                modifyUserPort.deactivateUser(getUserByIdInside(id).getUuid());
            }
        }
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
