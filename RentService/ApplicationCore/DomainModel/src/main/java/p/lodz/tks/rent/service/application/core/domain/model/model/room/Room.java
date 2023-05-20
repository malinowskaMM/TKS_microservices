package p.lodz.tks.rent.service.application.core.domain.model.model.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Room implements Serializable {

    private UUID uuid;
    private Integer roomNumber;
    private Double price;
    private Integer roomCapacity;


    public Room(Integer roomNumber, Double price, Integer roomCapacity) {
        this.uuid = UUID.randomUUID();
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomCapacity = roomCapacity;
    }

    public Room(UUID uuid, Integer roomNumber, Double price, Integer roomCapacity) {
        this.uuid = uuid;
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomCapacity = roomCapacity;
    }
}
