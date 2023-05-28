package p.lodz.tks.rent.service.rest.controller.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
public class ShowUserDto {

    private UUID uuid;
    @NotNull
    @Size(min = 5)
    private String login;

    @NotNull
    private String accessLevel;

    @JsonbCreator
    public ShowUserDto(@JsonbProperty("uuid")UUID uuid, @JsonbProperty("login")String login, @JsonbProperty("accessLevel")String accessLevel) {
        this.uuid = uuid;
        this.login = login;
        this.accessLevel = accessLevel;
    }

    public ShowUserDto() {
    }
}
