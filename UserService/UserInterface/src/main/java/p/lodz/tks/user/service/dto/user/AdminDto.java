package p.lodz.tks.user.service.rest.controller.dto.user;

import lombok.Getter;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

@Getter
public class AdminDto extends UserDto {

    @JsonbCreator
    public AdminDto(@JsonbProperty("login")String login, @JsonbProperty("password")String password, @JsonbProperty("accessLevel")String accessLevel) {
        super(login, password, accessLevel);
    }
}
