package p.lodz.tks.rent.service.repositories.modelEnt.user.client;

import lombok.Getter;
import lombok.Setter;
import p.lodz.tks.rent.service.repositories.modelEnt.user.AccessLevelEnt;
import p.lodz.tks.rent.service.repositories.modelEnt.user.UserEnt;

import java.util.UUID;

@Getter
public class ClientEnt extends UserEnt {

    private final String personalId;

    @Setter
    private String firstName;

    @Setter
    private String lastName;

    @Setter
    private String address;

    private Double moneySpent;

    public ClientEnt(String personalId, String firstName, String lastName, String address, String login, String password, AccessLevelEnt accessLevel) {
        super(true, login, password, accessLevel);
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.moneySpent = 0.0;
    }

    public ClientEnt(UUID uuid, String personalId, String firstName, String lastName, String address, String login, String password, AccessLevelEnt accessLevel) {
        super(uuid, true, login, password, accessLevel);
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.moneySpent = 0.0;
    }
}