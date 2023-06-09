package p.lodz.tks.rent.service.dto.rent;

import lombok.Getter;
import lombok.Setter;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class RentDto {

    @NotNull
    String login;

    @NotNull
    String roomId;

    @NotNull
    LocalDateTime beginTime;

    @NotNull
    LocalDateTime endTime;

    @JsonbCreator
    public RentDto(@JsonbProperty("login")String login, @JsonbProperty("roomId")String roomId, @JsonbProperty("startDate") LocalDateTime beginTime, @JsonbProperty("endDate") LocalDateTime endTime) {
        this.login = login;
        this.roomId = roomId;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

}
