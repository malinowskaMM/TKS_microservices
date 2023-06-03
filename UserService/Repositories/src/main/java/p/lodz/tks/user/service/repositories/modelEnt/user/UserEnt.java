package p.lodz.tks.user.service.repositories.modelEnt.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

public class UserEnt implements Serializable{

    @Getter
    private final UUID uuid;
    private boolean isActive;

    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private AccessLevelEnt accessLevel;

    public UserEnt(boolean isActive, String login, String password, AccessLevelEnt accessLevel) {
        this.uuid = UUID.randomUUID();
        this.isActive = isActive;
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public UserEnt(UUID uuid, boolean isActive, String login, String password, AccessLevelEnt accessLevel) {
        this.uuid = uuid;
        this.isActive = isActive;
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
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
