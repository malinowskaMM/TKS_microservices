package p.lodz.tks.rent.service.repositories.modelEnt.rent;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RentEnt implements Serializable {
    UUID id;
    LocalDateTime beginTime;
    LocalDateTime endTime;
    String login;
    String roomId;

    public RentEnt(LocalDateTime beginTime, LocalDateTime endTime, String login, String roomId) {
        this.id = UUID.randomUUID();
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.login = login;
        this.roomId = roomId;
    }

    public RentEnt(UUID uuid, LocalDateTime beginTime, LocalDateTime endTime, String login, String roomId) {
        this.id = uuid;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.login = login;
        this.roomId = roomId;
    }

    public void endRent() {
        this.endTime = LocalDateTime.now();
    }
}
