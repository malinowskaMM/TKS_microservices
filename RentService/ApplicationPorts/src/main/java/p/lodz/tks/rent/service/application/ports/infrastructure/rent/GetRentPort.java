package p.lodz.tks.rent.service.application.ports.infrastructure.rent;


import p.lodz.tks.rent.service.application.core.domain.model.model.rent.Rent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GetRentPort {

    List<Rent> getRentsByRoom(UUID roomId);
    List<Rent> getRentsByClient(String login);
    List<Rent> getCurrentRentsByRoom(UUID roomId, LocalDateTime beginTime, LocalDateTime endTime);
    List<Rent> getRents();
    Rent getRentById(UUID id);
}