package p.lodz.tks.rent.service.repositories.adapters.dto.room;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
public class RoomDtoEnt {

    @Positive
    private Integer roomNumber;

    @NotNull
    @PositiveOrZero
    private Double price;

    @NotNull
    @Positive
    private Integer roomCapacity;

    public RoomDtoEnt(Integer roomNumber, Double price, Integer roomCapacity) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomCapacity = roomCapacity;
    }
}
