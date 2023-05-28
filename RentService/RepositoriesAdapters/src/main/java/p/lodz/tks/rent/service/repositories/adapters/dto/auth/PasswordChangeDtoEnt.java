package p.lodz.tks.rent.service.repositories.adapters.dto.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PasswordChangeDtoEnt {

    @NotNull
    @Size(min = 5)
    String login;

    @NotNull
    @Size(min = 9)
    String oldPassword;

    @NotNull
    @Size(min = 9)
    String newPassword;

    @NotNull
    @Size(min = 9)
    String confirmNewPassword;

    public PasswordChangeDtoEnt(String login, String oldPassword, String newPassword, String confirmNewPassword) {
        this.login = login;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public PasswordChangeDtoEnt() {
    }
}
