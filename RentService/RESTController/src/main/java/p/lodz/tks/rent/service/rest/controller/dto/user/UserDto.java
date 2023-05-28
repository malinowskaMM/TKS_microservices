package p.lodz.tks.rent.service.rest.controller.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDto {

    @NotNull
    @Size(min = 5)
    private String login;

    @NotNull
    @Size(min = 9)
    private String password;

    @NotNull
    private String accessLevel;

    @JsonbCreator
    public UserDto(@JsonbProperty("login")String login, @JsonbProperty("password")String password, @JsonbProperty("accessLevel")String accessLevel) {
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
    }
}
