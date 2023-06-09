package p.lodz.tks.rent.service.soap.controller.interfaces;



import p.lodz.tks.rent.service.soap.controller.model.rent.RentSOAP;
import p.lodz.tks.rent.service.soap.controller.model.rent.RentSOAPException;
import p.lodz.tks.rent.service.soap.controller.model.room.RoomSOAP;

import jakarta.jws.WebService;
import java.util.List;

@WebService
public interface RentSoapAdapterInterface {


        public List<RentSOAP> getRents();


        public List<RoomSOAP> getFreeRooms();

        public List<RentSOAP> getRentsByRoom(String roomId);

        public List<RentSOAP> getRentsByClient(String login);

        public List<RentSOAP> getRentsByStartDate(String startDate);

        public List<RentSOAP> getRentsByEndDate(String endDate);

        public List<RentSOAP> getPastRents();

        public List<RentSOAP> getCurrentRentsByClientId(String login);

        public List<RentSOAP> getPastRentsByClientId(String login);

        public List<RentSOAP> getCurrentRentsByRoomId(String roomId);

        public List<RentSOAP> getPastRentsByRoomId(String roomId);

        public List<RentSOAP> getCurrentRents();

        public void rentRoom(RentSOAP rentSOAP) throws Exception;

        public void deleteRent(String rentId) throws RentSOAPException;

}