package p.lodz.tks.rent.service.soap.controller.adapters;


import p.lodz.tks.rent.service.rent.RentUseCase;
import p.lodz.tks.rent.service.soap.controller.interfaces.RentSoapAdapterInterface;
import p.lodz.tks.rent.service.soap.controller.model.rent.RentSOAP;
import p.lodz.tks.rent.service.soap.controller.model.rent.RentSOAPConverter;
import p.lodz.tks.rent.service.soap.controller.model.rent.RentSOAPException;
import p.lodz.tks.rent.service.soap.controller.model.room.RoomSOAP;
import p.lodz.tks.rent.service.soap.controller.model.room.RoomSOAPConverter;
import p.lodz.tks.rent.service.soap.controller.model.room.RoomSOAPException;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@WebService(endpointInterface  = "project.tks.soap.interfaces.RentSoapAdapterInterface")
@SOAPBinding
public class RentSoapAdapter implements RentSoapAdapterInterface {

    public RentSoapAdapter() {
    }

    @Inject
    private RentUseCase rentUseCase;

    @Inject
    private LocalDateTimeAdapter localDateTimeAdapter;


    public List<RentSOAP> getRents() {
        return rentUseCase.getRents().stream().map(rent -> {
            try{
                return RentSOAPConverter.convertRentToRentSOAP(rent);
            } catch (RentSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<RoomSOAP> getFreeRooms() {
        return rentUseCase.getCurrentFreeRooms().stream().map(room -> {
            try{
                return RoomSOAPConverter.convertRoomToRoomSOAP(room);
            } catch (RoomSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<RentSOAP> getRentsByRoom(String roomId) {
        UUID id = UUID.fromString(roomId);
        return rentUseCase.getRentsByRoomId(id).stream().map(rent -> {
            try{
                return RentSOAPConverter.convertRentToRentSOAP(rent);
            } catch (RentSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<RentSOAP> getRentsByClient(String login) {
        return rentUseCase.getRentsByClientId(login).stream().map(rent -> {
            try{
                return RentSOAPConverter.convertRentToRentSOAP(rent);
            } catch (RentSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<RentSOAP> getRentsByStartDate(String startDate) {
        LocalDateTime date = LocalDateTime.parse(startDate);
        return rentUseCase.getRentsByStartDate(date).stream().map(rent -> {
            try{
                return RentSOAPConverter.convertRentToRentSOAP(rent);
            } catch (RentSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<RentSOAP> getRentsByEndDate(String endDate) {
        LocalDateTime date = LocalDateTime.parse(endDate);
        return rentUseCase.getRentsByEndDate(date).stream().map(rent -> {
            try{
                return RentSOAPConverter.convertRentToRentSOAP(rent);
            } catch (RentSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<RentSOAP> getPastRents() {
        return rentUseCase.getPastRents().stream().map(rent -> {
            try{
                return RentSOAPConverter.convertRentToRentSOAP(rent);
            } catch (RentSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<RentSOAP> getCurrentRentsByClientId(String login) {
        return rentUseCase.getCurrentRentsByClientId(login).stream().map(rent -> {
            try{
                return RentSOAPConverter.convertRentToRentSOAP(rent);
            } catch (RentSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<RentSOAP> getPastRentsByClientId(String login) {
        return rentUseCase.getPastRentsByClientId(login).stream().map(rent -> {
            try{
                return RentSOAPConverter.convertRentToRentSOAP(rent);
            } catch (RentSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<RentSOAP> getCurrentRentsByRoomId(String roomId) {
        UUID id = UUID.fromString(roomId);
        return rentUseCase.getCurrentRentsByRoomId(id).stream().map(rent -> {
            try{
                return RentSOAPConverter.convertRentToRentSOAP(rent);
            } catch (RentSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<RentSOAP> getPastRentsByRoomId(String roomId) {
        UUID id = UUID.fromString(roomId);
        return rentUseCase.getPastRentsByRoomId(id).stream().map(rent -> {
            try{
                return RentSOAPConverter.convertRentToRentSOAP(rent);
            } catch (RentSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<RentSOAP> getCurrentRents() {
        return rentUseCase.getCurrentRents().stream().map(rent -> {
            try{
                return RentSOAPConverter.convertRentToRentSOAP(rent);
            } catch (RentSOAPException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public void rentRoom(RentSOAP rentSOAP) throws Exception {
        rentUseCase.rentRoom(rentSOAP.getRoomId(), localDateTimeAdapter.unmarshal(rentSOAP.getBeginTime()),
                localDateTimeAdapter.unmarshal(rentSOAP.getEndTime()));
    }

    public void deleteRent(String rentId) throws RentSOAPException {
        UUID id = UUID.fromString(rentId);
        if(rentUseCase.getRent(id) == null) {
            throw new RentSOAPException("Cannot find rent with given id");
        }
        rentUseCase.removeRent(id);
    }


}
