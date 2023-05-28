package p.lodz.tks.rent.service.repositories.adapters.aggregates.rent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import p.lodz.tks.rent.service.application.core.domain.model.model.rent.Rent;

import p.lodz.tks.rent.service.application.ports.control.rent.CreateRentPort;
import p.lodz.tks.rent.service.application.ports.control.rent.DeleteRentPort;
import p.lodz.tks.rent.service.application.ports.control.rent.EndRentPort;
import p.lodz.tks.rent.service.application.ports.infrastructure.rent.GetRentPort;
import p.lodz.tks.rent.service.repositories.adapters.aggregates.rent.converter.RentEntConverter;
import p.lodz.tks.rent.service.repositories.adapters.repositories.RentRepository;
import p.lodz.tks.rent.service.repositories.modelEnt.rent.RentEnt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RentRepositoryAdapter implements CreateRentPort, DeleteRentPort, EndRentPort, GetRentPort {

    @Inject
    RentRepository rentRepository;


    @Override
    public Rent createRent(LocalDateTime beginTime, LocalDateTime endTime, String clientId, String roomId) {
        return RentEntConverter.convertRentEntToRent(rentRepository.createRent(beginTime, endTime, clientId, roomId));
    }

    @Override
    public void removeRent(UUID id) {
        rentRepository.removeRent(id);
    }

    @Override
    public void endRent(UUID id) {
        rentRepository.endRent(id);
    }

    @Override
    public List<Rent> getRentsByRoom(UUID roomId) {
        List<RentEnt> rentsEnt = rentRepository.getRentsByRoom(roomId);
        List<Rent> rents = new ArrayList<>();

        for (RentEnt rentEnt : rentsEnt)
            rents.add(RentEntConverter.convertRentEntToRent(rentEnt));
        return rents;
    }

    @Override
    public List<Rent> getRentsByClient(String login) {
        List<RentEnt> rentsEnt = rentRepository.getRentsByClient(login);
        List<Rent> rents = new ArrayList<>();

        for (RentEnt rentEnt : rentsEnt)
            rents.add(RentEntConverter.convertRentEntToRent(rentEnt));
        return rents;
    }

    @Override
    public List<Rent> getCurrentRentsByRoom(UUID roomId, LocalDateTime beginTime, LocalDateTime endTime) {
        List<RentEnt> rentsEnt = rentRepository.getCurrentRentsByRoom(roomId, beginTime, endTime);
        List<Rent> rents = new ArrayList<>();

        for (RentEnt rentEnt : rentsEnt)
            rents.add(RentEntConverter.convertRentEntToRent(rentEnt));
        return rents;
    }

    @Override
    public List<Rent> getRents() {
        List<RentEnt> rentsEnt = rentRepository.getRents();
        List<Rent> rents = new ArrayList<>();

        for (RentEnt rentEnt : rentsEnt)
            rents.add(RentEntConverter.convertRentEntToRent(rentEnt));
        return rents;
    }

    @Override
    public Rent getRentById(UUID id) {
        return RentEntConverter.convertRentEntToRent(rentRepository.getRentById(id));
    }
}
