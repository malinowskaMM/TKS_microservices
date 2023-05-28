package p.lodz.tks.user.service.repositories.adapters.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDtoEnt {

    @NotNull
    @Size(min = 5)
    private String login;

    @NotNull
    @Size(min = 9)
    private String password;

    @NotNull
    private String accessLevel;

    public UserDtoEnt(String login, String password, String accessLevel) {
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
    }
}
