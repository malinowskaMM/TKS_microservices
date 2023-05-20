package p.lodz.tks.rent.service.soap.controller.model.room;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(namespace = "http://tks.soap.adapters.pl/room", name = "Room")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class RoomSOAP implements Serializable {

    private String uuid;
    private Integer roomNumber;
    private Double price;
    private Integer roomCapacity;

    public RoomSOAP(String uuid, Integer roomNumber, Double price, Integer roomCapacity) {
        this.uuid = uuid;
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomCapacity = roomCapacity;
    }

    public RoomSOAP() {
    }

    @Override
    public String toString() {
        return "RoomSOAP{" +
                "uuid=" + uuid +
                ", roomNumber=" + roomNumber +
                ", price=" + price +
                ", roomCapacity=" + roomCapacity +
                '}';
    }
}
