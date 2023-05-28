package p.lodz.tks.user.service.repositories.adapters.dto.user;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class ClientDtoEnt extends UserDtoEnt {

    @NotNull
    @Size(min = 11, max = 11)
    private String personalId;

    @NotNull
    @Size(max = 35)
    private String firstName;

    @NotNull
    @Size(max = 35)
    private String lastName;

    @NotNull
    private String address;


    public ClientDtoEnt(String login, String password, String accessLevel, String personalId, String firstName, String lastName, String address) {
        super(login, password, accessLevel);
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }
}
