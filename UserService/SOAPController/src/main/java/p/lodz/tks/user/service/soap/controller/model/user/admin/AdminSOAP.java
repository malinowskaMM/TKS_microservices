package p.lodz.tks.user.service.soap.controller.model.user.admin;


import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.soap.controller.model.user.UserSOAP;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

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
