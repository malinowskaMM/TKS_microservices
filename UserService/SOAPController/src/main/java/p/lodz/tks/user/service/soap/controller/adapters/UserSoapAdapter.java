package p.lodz.tks.user.service.soap.controller.adapters;


import p.lodz.tks.user.service.application.core.domain.model.exceptions.PasswordMatchFailed;
import p.lodz.tks.user.service.soap.controller.interfaces.UserSoapAdapterInterface;
import p.lodz.tks.user.service.soap.controller.model.user.PasswordChangeSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.ShowUserSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.UserSOAPConverter;
import p.lodz.tks.user.service.soap.controller.model.user.UserSOAPException;
import p.lodz.tks.user.service.soap.controller.model.user.admin.AdminSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.client.ClientSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.manager.ManagerSOAP;
import p.lodz.tks.user.service.user.UserUseCase;

import jakarta.inject.Inject;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

@WebService(endpointInterface = "project.tks.soap.interfaces.UserSoapAdapterInterface")
@SOAPBinding
public class UserSoapAdapter implements UserSoapAdapterInterface {

    public UserSoapAdapter() {
    }

    @Inject
    private UserUseCase userUseCase;


    public void changeUserPassword(PasswordChangeSOAP passwordChangeSOAP) {
        if (passwordChangeSOAP.getNewPassword().equals(passwordChangeSOAP.getConfirmNewPassword())) {
            userUseCase.changePassword(passwordChangeSOAP.getOldPassword(), passwordChangeSOAP.getNewPassword());
        } else {
            throw new PasswordMatchFailed("New password and new confrim password do not match");
        }
    }

    public void createClient(ClientSOAP clientSOAP) {
        userUseCase.registerClient(clientSOAP.getFirstName(), clientSOAP.getLastName(), clientSOAP.getPersonalId(), clientSOAP.getAddress(), clientSOAP.getLogin(), clientSOAP.getPassword());
    }

    public void createAdmin(AdminSOAP adminSOAP) {
        userUseCase.registerAdmin(adminSOAP.getLogin(), adminSOAP.getPassword());
    }

    public void createManager(ManagerSOAP managerSOAP){
        userUseCase.registerManager(managerSOAP.getLogin(), managerSOAP.getPassword());
    }

    public List<ShowUserSOAP> getUsers() {
        return userUseCase.getAllUsers().stream().map(user -> {
            try {
                return UserSOAPConverter.convertShowUserDtoToShowUserSOAP(user);
            } catch (InvocationTargetException | IllegalAccessException e) {
                return null;
            }
        }).toList();
    }

    public List<ShowUserSOAP> getUsersByPartOfLogin(String partOfLogin) {
        return userUseCase.findClientsByLoginPart(partOfLogin).stream().map(user -> {
            try {
                return UserSOAPConverter.convertShowUserDtoToShowUserSOAP(user);
            } catch (InvocationTargetException | IllegalAccessException e) {
                return null;
            }
        }).toList();
    }

    public void deleteUser(String userId) throws UserSOAPException {
        UUID id = UUID.fromString(userId);
        if (userUseCase.getUserById(id) == null) {
            throw new UserSOAPException("Cannot find user with given id");
        }
        userUseCase.deleteUser(id);
    }

    public ShowUserSOAP getUser(String userId) throws InvocationTargetException, UserSOAPException, IllegalAccessException {
        UUID id = UUID.fromString(userId);
        if (userUseCase.getUserById(id) == null) {
            throw new UserSOAPException("Cannot find user with given id");
        }
        return UserSOAPConverter.convertShowUserDtoToShowUserSOAP(userUseCase.getUserById(id));
    }

    public ClientSOAP getClient(String userId) throws UserSOAPException {
        UUID id = UUID.fromString(userId);
        if (userUseCase.getClientById(id) == null) {
            throw new UserSOAPException("Cannot find user with given id");
        }
        return UserSOAPConverter.convertClientToClientSOAP(userUseCase.getClientById(id));
    }
}