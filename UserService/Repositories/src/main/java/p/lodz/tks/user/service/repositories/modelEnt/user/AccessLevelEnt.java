package p.lodz.tks.user.service.repositories.modelEnt.user;

public enum AccessLevelEnt {

    ADMIN,
    CLIENT,
    MANAGER,

    NONE;

    public String getAccessLevel() {
        return this.name();
    }
}
