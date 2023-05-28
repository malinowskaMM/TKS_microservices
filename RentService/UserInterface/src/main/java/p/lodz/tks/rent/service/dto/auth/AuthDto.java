package p.lodz.tks.rent.service.dto.auth;

import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
