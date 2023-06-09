package p.lodz.tks.rent.service.soap.controller.model.user;

import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@Getter
@Setter
@XmlType(namespace = "http://tks.soap.adapters.pl/user/show", name = "ShowUser")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShowUserSOAP {
    private String uuid;
    private String login;

    private String accessLevel;

    public ShowUserSOAP(String uuid, String login, String accessLevel) {
        this.uuid = uuid;
        this.login = login;
        this.accessLevel = accessLevel;
    }

    public ShowUserSOAP() {
    }
}
