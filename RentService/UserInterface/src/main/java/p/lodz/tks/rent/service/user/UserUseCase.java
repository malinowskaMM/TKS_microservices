package p.lodz.tks.rent.service.user;

import com.nimbusds.jose.JOSEException;
import p.lodz.tks.rent.service.application.core.domain.model.dto.user.ShowUserDto;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.manager.Manager;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface UserUseCase {
    Client registerClient(String firstName, String lastName, String personalId, String address, String login, String password);
    Manager registerManager(String login, String password);
    Admin registerAdmin(String login, String password);
    void updateUser(UUID id, String jws, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel) throws ParseException, JOSEException;
    Client getClientById(UUID uuid);
    ShowUserDto getUserById(UUID id);
    User getUserByIdInside(UUID id);
    List<ShowUserDto> findClientsByLoginPart(String login);
    ShowUserDto findUserByLogin(String login, String password);
    List<ShowUserDto> findClients(Predicate<User> predicate);
    void activateUser(UUID id, String jws) throws JOSEException, ParseException;
    void deactivateUser(UUID id, String jws) throws JOSEException, ParseException;
    List<ShowUserDto> getAllUsers();
    List<User> getAllUsersInside();
    void deleteUser(UUID id);
    boolean changePassword(String oldPassword, String newPassword);
    User getUserFromServerContext();
}
