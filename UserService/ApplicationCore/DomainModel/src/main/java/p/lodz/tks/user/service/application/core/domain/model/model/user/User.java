package p.lodz.tks.user.service.application.core.domain.model.model.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class User implements Serializable{

    private UUID uuid;
    private boolean isActive;
    private String login;
    private String password;
    private AccessLevel accessLevel;

    public User(boolean isActive, String login, String password, AccessLevel accessLevel) {
        this.uuid = UUID.randomUUID();
        this.isActive = isActive;
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public User(UUID uuid, boolean isActive, String login, String password, AccessLevel accessLevel) {
        this.uuid = uuid;
        this.isActive = isActive;
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public User() {
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        isActive = true;
    }

    public void deactivate() {
        isActive = false;
    }

}
