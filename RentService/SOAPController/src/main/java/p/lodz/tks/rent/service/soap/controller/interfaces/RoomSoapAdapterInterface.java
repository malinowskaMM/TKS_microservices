package p.lodz.tks.rent.service.soap.controller.interfaces;


import p.lodz.tks.rent.service.soap.controller.model.room.RoomSOAP;
import p.lodz.tks.rent.service.soap.controller.model.room.RoomSOAPException;

import jakarta.jws.WebService;
import java.util.List;

@WebService
public interface RoomSoapAdapterInterface {

        public RoomSOAP getRoom(String uuid) throws RoomSOAPException;

        public List<RoomSOAP> getRooms();

        public void createRoom(RoomSOAP roomSOAP);

        public void deleteRoom(String roomId) throws RoomSOAPException;
}
