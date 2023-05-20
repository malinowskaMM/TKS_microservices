package p.lodz.tks.rent.service.repositories.modelEnt.user;

public enum AccessLevelEnt {

    ADMIN,
    CLIENT,
    MANAGER,
    NONE;

    public String getAccessLevel() {
        return this.name();
    }
}
