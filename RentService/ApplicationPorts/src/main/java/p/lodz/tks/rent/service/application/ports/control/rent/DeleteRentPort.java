package p.lodz.tks.rent.service.application.ports.control.rent;

import java.util.UUID;

public interface DeleteRentPort {

    void removeRent(UUID id);
}
