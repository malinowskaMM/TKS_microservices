package p.lodz.tks.user.service.application.core.domain.model.dto.user;

import lombok.Getter;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
public class ClientDto extends UserDto {

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


    @JsonbCreator
    public ClientDto(@JsonbProperty("login")String login, @JsonbProperty("password")String password, @JsonbProperty("accessLevel")String accessLevel, @JsonbProperty("personalId")String personalId, @JsonbProperty("firstName")String firstName, @JsonbProperty("lastName")String lastName, @JsonbProperty("address")String address) {
        super(login, password, accessLevel);
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }
}
