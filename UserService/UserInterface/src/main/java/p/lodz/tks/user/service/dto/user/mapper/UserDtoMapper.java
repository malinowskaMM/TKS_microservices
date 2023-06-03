package p.lodz.tks.user.service.dto.user.mapper;


import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
// import p.lodz.tks.user.service.application.core.domain.model.model.user.admin.Admin;
// import p.lodz.tks.user.service.application.core.domain.model.model.user.client.Client;
// import p.lodz.tks.user.service.application.core.domain.model.model.user.manager.Manager;
import p.lodz.tks.user.service.dto.user.*;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDtoMapper {

    public User toUser(UserDto userDto) {
        return new User(
                true,
                userDto.getLogin(),
                userDto.getPassword(),
                AccessLevel.valueOf(userDto.getAccessLevel())
        );
    }

    public static ShowUserDto toShowUserDto(User user) {
        return new ShowUserDto(user.getUuid() ,user.getLogin(), user.getAccessLevel().getAccessLevel());
    }

}
