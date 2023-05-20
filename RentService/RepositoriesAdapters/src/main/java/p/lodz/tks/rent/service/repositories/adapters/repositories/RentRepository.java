package p.lodz.tks.rent.service.repositories.adapters.repositories;

import p.lodz.tks.rent.service.application.core.domain.model.exceptions.RoomNotAvailable;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.rent.service.repositories.modelEnt.rent.RentEnt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RentRepository {
    RentEnt createRent(LocalDateTime beginTime, LocalDateTime endTime, String clientId, String roomId) throws RoomNotAvailable;
    void removeRent(UUID id);
    void endRent(UUID id);
    List<RentEnt> getRentsByRoom(UUID roomId);
    List<RentEnt> getRentsByClient(String login);
    List<RentEnt> getCurrentRentsByRoom(UUID roomId, LocalDateTime beginTime, LocalDateTime endTime);
    List<RentEnt> getRents();
    RentEnt getRentById(UUID id);
}
