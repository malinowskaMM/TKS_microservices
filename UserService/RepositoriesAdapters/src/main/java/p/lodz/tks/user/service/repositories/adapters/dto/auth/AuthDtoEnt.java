package p.lodz.tks.user.service.repositories.adapters.dto.auth;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class AuthDtoEnt {
    @NotNull
    @Size(min = 5)
    String login;

    @NotNull
    @Size(min = 9)
    String password;

    public AuthDtoEnt(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public AuthDtoEnt() {
    }
}
