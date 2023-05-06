package p.lodz.tks.user.service.soap.controller.model.user.client;

import lombok.Getter;
import lombok.Setter;
import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.soap.controller.model.user.UserSOAP;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://tks.soap.adapters.pl/user/client", name = "Client")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class ClientSOAP extends UserSOAP {

    private String personalId;

    @Setter
    private String firstName;

    @Setter
    private String lastName;

    @Setter
    private String address;

    private Double moneySpent;

    public ClientSOAP(String personalId, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel) {
        super(true, login, password, accessLevel);
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.moneySpent = 0.0;
    }

    public ClientSOAP(String uuid, String personalId, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel) {
        super(uuid, true, login, password, accessLevel);
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.moneySpent = 0.0;
    }

    public ClientSOAP() {
    }
}
