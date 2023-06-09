package p.lodz.tks.rent.service.soap.controller.adapters;


import p.lodz.tks.rent.service.room.RoomUseCase;
import p.lodz.tks.rent.service.soap.controller.interfaces.RoomSoapAdapterInterface;
import p.lodz.tks.rent.service.soap.controller.model.room.RoomSOAP;
import p.lodz.tks.rent.service.soap.controller.model.room.RoomSOAPConverter;
import p.lodz.tks.rent.service.soap.controller.model.room.RoomSOAPException;

import jakarta.inject.Inject;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import java.util.List;
import java.util.UUID;

@WebService(endpointInterface = "project.tks.soap.interfaces.RoomSoapAdapterInterface")
@SOAPBinding
public class RoomSoapAdapter implements RoomSoapAdapterInterface {



    @Inject
    private RoomUseCase roomUseCase;

    public RoomSOAP getRoom(String uuid) throws RoomSOAPException {
        UUID id = UUID.fromString(uuid);
        return RoomSOAPConverter.convertRoomToRoomSOAP(roomUseCase.getRoomById(id));
    }

    public List<RoomSOAP> getRooms() {
        return roomUseCase.getAllRooms().stream().map( room -> {
            try {
                return RoomSOAPConverter.convertRoomToRoomSOAP(room);
            } catch (RoomSOAPException e) {
                e.printStackTrace();
            }
            return null;
        }).toList();
    }

    public void createRoom(RoomSOAP roomSOAP) {
        roomUseCase.createRoom(roomSOAP.getRoomNumber(), roomSOAP.getPrice(), roomSOAP.getRoomCapacity());
    }

    public void deleteRoom(@WebParam String uuid) throws RoomSOAPException {
        UUID id = UUID.fromString(uuid);
        if (roomUseCase.getRoomById(id) == null) {
            throw new RoomSOAPException("Room with given id not found.");
        }
        roomUseCase.removeRoom(id);

    }
}