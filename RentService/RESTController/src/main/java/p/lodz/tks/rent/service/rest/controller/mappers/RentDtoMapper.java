package p.lodz.tks.rent.service.rest.controller.mappers;

import lombok.NoArgsConstructor;
import p.lodz.tks.rent.service.application.core.application.services.services.services.UserService;
import p.lodz.tks.rent.service.rest.controller.dto.rent.RentDto;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.ClientWithGivenIdNotFound;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.RoomWithGivenIdNotFound;
import p.lodz.tks.rent.service.application.core.domain.model.model.rent.Rent;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;


@NoArgsConstructor
@Stateless
public class RentDtoMapper {

     @Inject
     UserService userService;

     public Rent toRent(RentDto rentDto) throws RoomWithGivenIdNotFound, ClientWithGivenIdNotFound {
         User client = userService.getAllUsersInside().stream().filter(user -> user.getLogin().equals(rentDto.getLogin())).toList().get(0);
         return new Rent(rentDto.getBeginTime(), rentDto.getEndTime(), client.getUuid().toString() ,rentDto.getRoomId());
     }

}
