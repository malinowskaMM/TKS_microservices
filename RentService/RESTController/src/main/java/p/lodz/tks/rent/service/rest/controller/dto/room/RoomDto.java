package p.lodz.tks.rent.service.rest.controller.dto.room;

import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
public class RoomDto {

    @Positive
    private Integer roomNumber;

    @NotNull
    @PositiveOrZero
    private Double price;

    @NotNull
    @Positive
    private Integer roomCapacity;

    @JsonbCreator
    public RoomDto(@JsonbProperty("roomNumber")Integer roomNumber, @JsonbProperty("price")Double price, @JsonbProperty("roomCapacity")Integer roomCapacity) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomCapacity = roomCapacity;
    }
}
