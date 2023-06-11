package p.lodz.tks.user.service.rest.controller.mappers;

import javax.ejb.Stateless;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.rest.controller.dto.user.*;

import java.util.UUID;

@NoArgsConstructor
@Stateless
public class UserDtoMapper {
    public static User toUser(UserDto userDto) {
        if (userDto.getUuid() != null) {
            return new User(
                    UUID.fromString(userDto.getUuid()),
                    true,
                    userDto.getLogin(),
                    userDto.getPassword(),
                    AccessLevel.valueOf(userDto.getAccessLevel())
            );
        }
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
