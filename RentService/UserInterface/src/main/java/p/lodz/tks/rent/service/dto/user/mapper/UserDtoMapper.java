package p.lodz.tks.rent.service.dto.user.mapper;

import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.manager.Manager;
import p.lodz.tks.rent.service.dto.user.*;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDtoMapper {

    public User toUser(UserDto userDto) {
        if(userDto instanceof AdminDto) {
            return new Admin(userDto.getLogin(), userDto.getPassword(), AccessLevel.valueOf(userDto.getAccessLevel()));
        } else if(userDto instanceof ManagerDto) {
            return new Manager(userDto.getLogin(), userDto.getPassword(), AccessLevel.valueOf(userDto.getAccessLevel()));
        } else if(userDto instanceof ClientDto) {
            return new Client(((ClientDto) userDto).getPersonalId(), ((ClientDto) userDto).getFirstName(), ((ClientDto) userDto).getLastName(), ((ClientDto) userDto).getAddress(), userDto.getLogin(), userDto.getPassword(), AccessLevel.valueOf(userDto.getAccessLevel()));
        }
        return null;
    }

    public static ShowUserDto toShowUserDto(User user) {
        return new ShowUserDto(user.getUuid() ,user.getLogin(), user.getAccessLevel().getAccessLevel());
    }

}
