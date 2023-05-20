package p.lodz.tks.rent.service.application.core.domain.model.model.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

public abstract class User implements Serializable{

    @Getter
    private UUID uuid;
    private boolean isActive;

    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private AccessLevel accessLevel;

    protected User(boolean isActive, String login, String password, AccessLevel accessLevel) {
        this.uuid = UUID.randomUUID();
        this.isActive = isActive;
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    protected User(UUID uuid, boolean isActive, String login, String password, AccessLevel accessLevel) {
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
