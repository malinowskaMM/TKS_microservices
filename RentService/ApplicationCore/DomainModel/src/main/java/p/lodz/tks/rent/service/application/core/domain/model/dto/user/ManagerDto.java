package p.lodz.tks.rent.service.application.core.domain.model.dto.user;

import lombok.Getter;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

@Getter
public class ManagerDto extends UserDto{

    @JsonbCreator
    public ManagerDto(@JsonbProperty("login")String login, @JsonbProperty("password")String password, @JsonbProperty("accessLevel")String accessLevel) {
        super(login, password, accessLevel);
    }
}
