package p.lodz.tks.rent.service.application.ports.control.rent;


import p.lodz.tks.rent.service.application.core.domain.model.model.rent.Rent;

import java.time.LocalDateTime;

public interface CreateRentPort {

    Rent createRent(LocalDateTime beginTime, LocalDateTime endTime, String clientId, String roomId);
}