package p.lodz.tks.user.service.soap.controller.model.user;

import lombok.Getter;
import lombok.Setter;
import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.UUID;

@XmlType(namespace = "http://tks.soap.adapters.pl/user", name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class UserSOAP implements Serializable {

    @Getter
    private String uuid;

    private boolean isActive;

    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private AccessLevel accessLevel;

    public UserSOAP(boolean isActive, String login, String password, AccessLevel accessLevel) {
        this.uuid = UUID.randomUUID().toString();
        this.isActive = isActive;
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public UserSOAP(String uuid, boolean isActive, String login, String password, AccessLevel accessLevel) {
        this.uuid = uuid;
        this.isActive = isActive;
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public UserSOAP() {
    }

    @Override
    public String toString() {
        return "UserSOAP{" +
                "uuid=" + uuid +
                ", isActive=" + isActive +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", accessLevel=" + accessLevel +
                '}';
    }
}
