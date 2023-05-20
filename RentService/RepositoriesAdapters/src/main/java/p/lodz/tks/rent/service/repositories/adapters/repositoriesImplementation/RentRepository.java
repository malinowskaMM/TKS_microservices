package p.lodz.tks.rent.service.repositories.adapters.repositoriesImplementation;


import p.lodz.tks.rent.service.application.core.domain.model.exceptions.RoomNotAvailable;
import p.lodz.tks.rent.service.repositories.modelEnt.rent.RentEnt;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.synchronizedList;

@ApplicationScoped
public class RentRepository implements p.lodz.tks.rent.service.repositories.adapters.repositories.RentRepository {

    private final List<RentEnt> rents = synchronizedList(new ArrayList<>());

    @Override
    public RentEnt createRent(LocalDateTime beginTime, LocalDateTime endTime, String clientId, String roomId) throws RoomNotAvailable {
        if(getCurrentRentsByRoom(UUID.fromString(roomId), beginTime, endTime).isEmpty()) {
            RentEnt rent = new RentEnt(beginTime, endTime, clientId, roomId);
            rents.add(rent);
            return rent;
        }
        throw new RoomNotAvailable("Cannot rent room");
    }

    @Override
    public void removeRent(UUID id) {
        RentEnt removed = getRentById(id);
        if(removed != null) {
            rents.remove(removed);
        }
    }

    @Override
    public void endRent(UUID id) {
        RentEnt rent = getRentById(id);
        if(rent != null) {
            rent.endRent();
        }
    }

    @Override
    public List<RentEnt> getRentsByClient(String login) {
        return getRents().stream().filter(rent -> rent.getLogin().equals(login)).toList();
    }

    @Override
    public List<RentEnt> getRentsByRoom(UUID roomId) {
        return getRents().stream().filter(rent -> rent.getRoomId().equals(roomId.toString())).toList();
    }


    @Override
    public List<RentEnt> getCurrentRentsByRoom(UUID roomId, LocalDateTime beginTime, LocalDateTime endTime) {
        return getRentsByRoom(roomId).stream().filter(
                rent -> ((rent.getBeginTime().isBefore(beginTime) && rent.getEndTime().isBefore(endTime) && rent.getEndTime().isAfter(beginTime))
                || (rent.getBeginTime().isAfter(beginTime) && rent.getEndTime().isBefore(endTime))
                || (rent.getBeginTime().isAfter(beginTime) && rent.getBeginTime().isBefore(endTime) && rent.getEndTime().isAfter(endTime))
        )).toList();
    }

    @Override
    public List<RentEnt> getRents() {
        return rents.stream().toList();
    }

    @Override
    public RentEnt getRentById(UUID id) {
        Optional<RentEnt> rentOptional = rents.stream().filter(rent -> rent.getId().equals(id)).findFirst();
        return rentOptional.orElse(null);
    }
}
