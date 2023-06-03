package p.lodz.tks.user.service.soap.controller.interfaces;


import p.lodz.tks.user.service.soap.controller.model.user.PasswordChangeSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.ShowUserSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.UserSOAPException;
import p.lodz.tks.user.service.soap.controller.model.user.admin.AdminSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.client.ClientSOAP;
import p.lodz.tks.user.service.soap.controller.model.user.manager.ManagerSOAP;

import javax.jws.WebService;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@WebService
public interface UserSoapAdapterInterface {


        public void changeUserPassword(PasswordChangeSOAP passwordChangeSOAP);

        public List<ShowUserSOAP> getUsers();

        public List<ShowUserSOAP> getUsersByPartOfLogin(String partOfLogin);

        public void deleteUser(String userId) throws UserSOAPException;

        public ShowUserSOAP getUser(String userId) throws InvocationTargetException, UserSOAPException, IllegalAccessException;

        public ClientSOAP getClient(String userId) throws UserSOAPException;

}
