package p.lodz.tks.rent.service.repositories.adapters.dto.user;

import lombok.Getter;

@Getter
public class AdminDtoEnt extends UserDtoEnt {

    public AdminDtoEnt(String login, String password, String accessLevel) {
        super(login, password, accessLevel);
    }
}
