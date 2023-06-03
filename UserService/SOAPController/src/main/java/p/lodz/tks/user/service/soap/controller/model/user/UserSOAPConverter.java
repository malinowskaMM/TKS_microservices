package p.lodz.tks.user.service.soap.controller.model.user;

import org.apache.commons.beanutils.BeanUtils;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.dto.user.ShowUserDto;
import p.lodz.tks.user.service.soap.controller.model.user.admin.AdminSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.client.ClientSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.manager.ManagerSOAP;


import java.lang.reflect.InvocationTargetException;

public class UserSOAPConverter {

    public static User convertClientSOAPToClient(ClientSOAP clientSOAP) throws UserSOAPException {
        try {
            User user = new User();
            return copyClientSOAPToClient(user, clientSOAP);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new UserSOAPException("Cannot convert soap client to domain client");
        }
    }

    private static User copyClientSOAPToClient(User user, ClientSOAP clientSOAP) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(user, clientSOAP);
        return user;
    }

    public static ClientSOAP convertClientToClientSOAP(User user) throws UserSOAPException {
        try {
            ClientSOAP clientSOAP = new ClientSOAP();
            return copyClientToClientSOAP(clientSOAP, user);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new UserSOAPException("Cannot convert domain client to soap client");
        }
    }

    private static ClientSOAP copyClientToClientSOAP(ClientSOAP clientSOAP, User user) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(clientSOAP, user);
        return clientSOAP;
    }

    public static User convertUserSOAPToUser(UserSOAP userSOAP) throws UserSOAPException {
        try {
            User user = new User();
            BeanUtils.copyProperties(user, userSOAP);
            return user;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new UserSOAPException("Cannot convert soap client to domain client");
        }
    }

    public static UserSOAP convertUserToUserSOAP(User user) throws UserSOAPException {
        try {
            UserSOAP userSOAP = new AdminSOAP();
            BeanUtils.copyProperties(userSOAP, userSOAP);
            return userSOAP;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new UserSOAPException("Cannot convert domain client to soap client");
        }
    }

    public static ShowUserSOAP convertShowUserDtoToShowUserSOAP(ShowUserDto showUserDto) throws InvocationTargetException, IllegalAccessException {
        ShowUserSOAP showUserSOAP = new ShowUserSOAP();
        BeanUtils.copyProperties(showUserSOAP, showUserDto);
        return showUserSOAP;
    }

    public static ShowUserDto convertShowUserSOAPToShowUserDto(ShowUserSOAP showUserSOAP) throws InvocationTargetException, IllegalAccessException {
        ShowUserDto showUserDto = new ShowUserDto();
        BeanUtils.copyProperties(showUserDto, showUserSOAP);
        return showUserDto;
    }

}
