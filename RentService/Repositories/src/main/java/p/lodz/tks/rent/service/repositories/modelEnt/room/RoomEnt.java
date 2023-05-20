package p.lodz.tks.rent.service.repositories.modelEnt.room;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RoomEnt implements Serializable {

    private UUID uuid;
    private Integer roomNumber;
    private Double price;
    private Integer roomCapacity;

    public RoomEnt(Integer roomNumber, Double price, Integer roomCapacity) {
        this.uuid = UUID.randomUUID();
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomCapacity = roomCapacity;
    }

    public RoomEnt(UUID uuid, Integer roomNumber, Double price, Integer roomCapacity) {
        this.uuid = uuid;
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomCapacity = roomCapacity;
    }
}
