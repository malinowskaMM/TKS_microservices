package p.lodz.tks.user.service.soap.controller.model.user;

import org.apache.commons.beanutils.BeanUtils;
import p.lodz.tks.user.service.application.core.domain.model.dto.user.ShowUserDto;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.user.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.user.service.application.core.domain.model.model.user.manager.Manager;
import p.lodz.tks.user.service.soap.controller.model.user.admin.AdminSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.client.ClientSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.manager.ManagerSOAP;


import java.lang.reflect.InvocationTargetException;

public class UserSOAPConverter {

    public static Client convertClientSOAPToClient(ClientSOAP clientSOAP) throws UserSOAPException {
        try {
            Client client = new Client();
            return copyClientSOAPToClient(client, clientSOAP);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new UserSOAPException("Cannot convert soap client to domain client");
        }
    }

    private static Client copyClientSOAPToClient(Client client, ClientSOAP clientSOAP) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(client, clientSOAP);
        return client;
    }

    public static ClientSOAP convertClientToClientSOAP(Client client) throws UserSOAPException {
        try {
            ClientSOAP clientSOAP = new ClientSOAP();
            return copyClientToClientSOAP(clientSOAP, client);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new UserSOAPException("Cannot convert domain client to soap client");
        }
    }

    private static ClientSOAP copyClientToClientSOAP(ClientSOAP clientSOAP, Client client) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(clientSOAP, client);
        return clientSOAP;
    }

    public static User convertUserSOAPToUser(UserSOAP userSOAP) throws UserSOAPException {
        try {
            if(userSOAP instanceof ClientSOAP) {
                User user = new Client();
                BeanUtils.copyProperties(user, userSOAP);
                return user;
            } if (userSOAP instanceof ManagerSOAP) {
                User user = new Manager();
                BeanUtils.copyProperties(user, userSOAP);
                return user;
            } else {
                User user = new Admin();
                BeanUtils.copyProperties(user, userSOAP);
                return user;
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new UserSOAPException("Cannot convert soap client to domain client");
        }
    }

    public static UserSOAP convertUserToUserSOAP(User user) throws UserSOAPException {
        try {
            if(user instanceof Client) {
                UserSOAP userSOAP = new ClientSOAP();
                BeanUtils.copyProperties(userSOAP, userSOAP);
                return userSOAP;
            } if (user instanceof Manager) {
                UserSOAP userSOAP = new ManagerSOAP();
                BeanUtils.copyProperties(userSOAP, userSOAP);
                return userSOAP;
            } else {
                UserSOAP userSOAP = new AdminSOAP();
                BeanUtils.copyProperties(userSOAP, userSOAP);
                return userSOAP;
            }
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
