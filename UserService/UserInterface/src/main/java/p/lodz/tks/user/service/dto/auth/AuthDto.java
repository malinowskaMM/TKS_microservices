package p.lodz.tks.user.service.dto.auth;

import lombok.Getter;
import lombok.Setter;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class AuthDto {
    @NotNull
    @Size(min = 5)
    String login;

    @NotNull
    @Size(min = 9)
    String password;

    @JsonbCreator
    public AuthDto(@JsonbProperty("login")String login, @JsonbProperty("password")String password) {
        this.login = login;
        this.password = password;
    }

    public AuthDto() {
    }
}
