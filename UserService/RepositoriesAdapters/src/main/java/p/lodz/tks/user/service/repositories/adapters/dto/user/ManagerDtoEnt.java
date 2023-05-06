package p.lodz.tks.user.service.repositories.adapters.dto.user;

import lombok.Getter;

@Getter
public class ManagerDtoEnt extends UserDtoEnt {

    public ManagerDtoEnt(String login, String password, String accessLevel) {
        super(login, password, accessLevel);
    }
}
