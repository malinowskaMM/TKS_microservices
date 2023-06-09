package p.lodz.tks.rent.service.soap.controller.model.user.manager;


import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.soap.controller.model.user.UserSOAP;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://tks.soap.adapters.pl/user/manager", name = "Manager")
@XmlAccessorType(XmlAccessType.FIELD)
public class ManagerSOAP extends UserSOAP {
    public ManagerSOAP(String login, String password, AccessLevel accessLevel) {
        super(true, login, password, accessLevel);
    }

    public ManagerSOAP(String uuid, String login, String password, AccessLevel accessLevel) {
        super(uuid, true, login, password, accessLevel);
    }

    public ManagerSOAP() {
    }
}
