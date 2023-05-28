package p.lodz.tks.user.service.soap.controller.model.user;

import javax.json.bind.annotation.JsonbCreator;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@Getter
@Setter
@XmlType(namespace = "http://tks.soap.adapters.pl/user/password", name = "Password")
@XmlAccessorType(XmlAccessType.FIELD)
public class PasswordChangeSOAP {
    String login;

    String oldPassword;

    String newPassword;

    String confirmNewPassword;

    @JsonbCreator
    public PasswordChangeSOAP(String login, String oldPassword, String newPassword, String confirmNewPassword) {
        this.login = login;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public PasswordChangeSOAP() {
    }
}
