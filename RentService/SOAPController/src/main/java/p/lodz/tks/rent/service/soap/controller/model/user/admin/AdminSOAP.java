package p.lodz.tks.rent.service.soap.controller.model.user.admin;



import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.soap.controller.model.user.UserSOAP;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://tks.soap.adapters.pl/user/admin", name = "Admin")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdminSOAP extends UserSOAP {

    public AdminSOAP(String login, String password, AccessLevel accessLevel) {
        super(true, login, password, accessLevel);
    }

    public AdminSOAP(String uuid, String login, String password, AccessLevel accessLevel) {
        super(uuid, true, login, password, accessLevel);
    }

    public AdminSOAP() {
    }
}
