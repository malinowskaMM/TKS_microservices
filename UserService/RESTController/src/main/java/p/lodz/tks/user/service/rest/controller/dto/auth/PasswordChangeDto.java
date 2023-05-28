package p.lodz.tks.user.service.rest.controller.dto.auth;

import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PasswordChangeDto {

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

    @JsonbCreator
    public PasswordChangeDto(@JsonbProperty("login")String login, @JsonbProperty("oldPassword")String oldPassword, @JsonbProperty("newPassword")String newPassword, @JsonbProperty("confirmNewPassword")String confirmNewPassword) {
        this.login = login;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public PasswordChangeDto() {
    }
}
