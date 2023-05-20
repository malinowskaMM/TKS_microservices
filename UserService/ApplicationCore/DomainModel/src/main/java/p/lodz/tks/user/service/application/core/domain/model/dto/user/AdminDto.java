package p.lodz.tks.user.service.application.core.domain.model.dto.user;

import lombok.Getter;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

@Getter
public class AdminDto extends UserDto {

    @JsonbCreator
    public AdminDto(@JsonbProperty("login")String login, @JsonbProperty("password")String password, @JsonbProperty("accessLevel")String accessLevel) {
        super(login, password, accessLevel);
    }
}
