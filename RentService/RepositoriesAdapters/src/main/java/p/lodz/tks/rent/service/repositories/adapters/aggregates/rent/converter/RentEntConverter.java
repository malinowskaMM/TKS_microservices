package p.lodz.tks.rent.service.repositories.adapters.aggregates.rent.converter;


import p.lodz.tks.rent.service.application.core.domain.model.model.rent.Rent;
import p.lodz.tks.rent.service.repositories.modelEnt.rent.RentEnt;

public class RentEntConverter {

    public static Rent convertRentEntToRent(RentEnt rentEnt) {
        if(rentEnt == null) {
            return null;
        }
        return new Rent(rentEnt.getId(), rentEnt.getBeginTime(), rentEnt.getEndTime(), rentEnt.getLogin(), rentEnt.getRoomId());
    }

    public static RentEnt convertRentToRentEnt(Rent rent) {
        return new RentEnt(rent.getId(), rent.getBeginTime(), rent.getEndTime(), rent.getLogin(), rent.getRoomId());
    }

}
