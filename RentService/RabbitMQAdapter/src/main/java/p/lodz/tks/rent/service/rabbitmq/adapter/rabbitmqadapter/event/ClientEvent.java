package p.lodz.tks.rent.service.rabbitmq.adapter.rabbitmqadapter.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.pl.NIP;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEvent {

    private boolean isEditEvent;

    @NotNull
    @Pattern(regexp = "[^\\d\\s!?_#$%^&*()@=+,.|/~`'\"\\\\]+")
    private String firstName;

    @NotNull
    @Pattern(regexp = "[^\\d\\s!?_#$%^&*()@=+,.|/~`'\"\\\\]+")
    private String lastName;

    @NotNull
    @Pattern(regexp = "[0-9]+")
    private String personalID;

    @NotNull
    private String address;

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    AccessLevel accessLevel;

    public ClientEvent(Client client) {
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.address = client.getAddress();
        this.personalID = client.getPersonalId();
        this.login = client.getLogin();
        this.password = client.getPassword();
        this.accessLevel = client.getAccessLevel();
    }

    public Client toClient() {
        return new Client(personalID, firstName, lastName, address, login, password, accessLevel);
    }
}
