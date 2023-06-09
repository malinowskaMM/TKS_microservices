package p.lodz.tks.rent.service.rest.controller.dto.user;

import lombok.Getter;
import lombok.Setter;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
