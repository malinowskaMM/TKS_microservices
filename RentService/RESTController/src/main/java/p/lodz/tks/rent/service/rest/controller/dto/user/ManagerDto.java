package p.lodz.tks.rent.service.rest.controller.dto.user;

import lombok.Getter;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

@Getter
public class ManagerDto extends UserDto{

    @JsonbCreator
    public ManagerDto(@JsonbProperty("login")String login, @JsonbProperty("password")String password, @JsonbProperty("accessLevel")String accessLevel) {
        super(login, password, accessLevel);
    }
}
