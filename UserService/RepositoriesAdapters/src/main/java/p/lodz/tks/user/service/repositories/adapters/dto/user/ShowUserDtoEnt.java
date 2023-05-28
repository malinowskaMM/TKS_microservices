package p.lodz.tks.user.service.repositories.adapters.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
public class ShowUserDtoEnt {

    private UUID uuid;
    @NotNull
    @Size(min = 5)
    private String login;

    @NotNull
    private String accessLevel;

    public ShowUserDtoEnt(UUID uuid, String login, String accessLevel) {
        this.uuid = uuid;
        this.login = login;
        this.accessLevel = accessLevel;
    }
}
