package p.lodz.tks.rent.service.soap.controller.model.rent;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@XmlType(namespace = "http://tks.soap.adapters.pl/rent", name = "Rent")
@XmlAccessorType(XmlAccessType.FIELD)
public class RentSOAP {
    String id;
    String beginTime;
    String endTime;
    String login;
    String roomId;

    public RentSOAP(String beginTime, String endTime, String login, String roomId) {
        this.id = UUID.randomUUID().toString();
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.login = login;
        this.roomId = roomId;
    }

    public RentSOAP(String uuid, String beginTime, String endTime, String login, String roomId) {
        this.id = uuid;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.login = login;
        this.roomId = roomId;
    }

    public RentSOAP() {
    }

    public void endRent() {
        this.endTime = LocalDateTime.now().toString();
    }
}
