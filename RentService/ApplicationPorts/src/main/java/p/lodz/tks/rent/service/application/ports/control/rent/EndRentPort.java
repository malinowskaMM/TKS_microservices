package p.lodz.tks.rent.service.application.ports.control.rent;

import java.util.UUID;

public interface EndRentPort {

    void endRent(UUID id);
}