package p.lodz.tks.rent.service.application.core.domain.model.model.user.client;

import lombok.Getter;
import lombok.Setter;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;

import java.util.UUID;

@Getter
public class Client extends User {

    private String personalId;

    @Setter
    private String firstName;

    @Setter
    private String lastName;

    @Setter
    private String address;

    @Setter
    private Double moneySpent;

    public Client(String personalId, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel) {
        super(true, login, password, accessLevel);
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.moneySpent = 0.0;
    }

    public Client(UUID uuid, String personalId, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel) {
        super(uuid, true, login, password, accessLevel);
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.moneySpent = 0.0;
    }
    public Client() {
    }
}