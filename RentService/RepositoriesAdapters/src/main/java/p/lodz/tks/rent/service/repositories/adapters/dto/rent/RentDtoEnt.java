package p.lodz.tks.rent.service.repositories.adapters.dto.rent;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class RentDtoEnt {

    @NotNull
    String login;

    @NotNull
    String roomId;

    @NotNull
    LocalDateTime beginTime;

    @NotNull
    LocalDateTime endTime;

    public RentDtoEnt(String login, String roomId, LocalDateTime beginTime, LocalDateTime endTime) {
        this.login = login;
        this.roomId = roomId;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

}
