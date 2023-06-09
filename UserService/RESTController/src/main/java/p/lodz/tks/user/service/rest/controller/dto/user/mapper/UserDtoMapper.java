package p.lodz.tks.user.service.rest.controller.dto.user.mapper;


import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.rest.controller.dto.user.*;

import jakarta.enterprise.context.ApplicationScoped;

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
