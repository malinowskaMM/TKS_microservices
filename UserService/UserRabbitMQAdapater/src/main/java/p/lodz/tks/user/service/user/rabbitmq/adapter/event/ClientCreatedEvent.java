package p.lodz.tks.user.service.user.rabbitmq.adapter.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.client.Client;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreatedEvent {

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

    public ClientCreatedEvent(Client client) {
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.address = client.getAddress();
        this.personalID = client.getPersonalId();
        this.login = client.getLogin();
        this.password = client.getPassword();
        this.accessLevel = client.getAccessLevel();
    }

}
