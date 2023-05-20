package p.lodz.tks.rent.service.application.core.domain.model.model.user;

public enum AccessLevel {

    ADMIN,
    CLIENT,
    MANAGER,

    NONE;

    public String getAccessLevel() {
        return this.name();
    }
}
