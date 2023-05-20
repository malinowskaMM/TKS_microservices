package p.lodz.tks.rent.service.soap.controller.model.rent;

import org.apache.commons.beanutils.BeanUtils;
import p.lodz.tks.rent.service.application.core.domain.model.model.rent.Rent;


import java.lang.reflect.InvocationTargetException;

public class RentSOAPConverter {

    public static RentSOAP convertRentToRentSOAP(Rent rent) throws RentSOAPException {
        try {
            RentSOAP rentSOAP = new RentSOAP();
            return copyRentToRentSOAP(rentSOAP, rent);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RentSOAPException("Cannot convert domain rent to soap rent");
        }
    }

    private static RentSOAP copyRentToRentSOAP(RentSOAP rentSOAP,Rent rent) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(rentSOAP, rent);
        return rentSOAP;
    }


    public static Rent convertRentSOAPToRent(RentSOAP rentSOAP) throws RentSOAPException {
        try {
            Rent rent = new Rent();
            return copyRentSOAPToRent(rent, rentSOAP);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RentSOAPException("Cannot convert soap rent to domain rent");
        }
    }

    private static Rent copyRentSOAPToRent(Rent rent, RentSOAP rentSOAP) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(rent, rentSOAP);
        return rent;
    }
}
