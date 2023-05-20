package p.lodz.tks.rent.service.soap.controller.model.room;

import org.apache.commons.beanutils.BeanUtils;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;

import java.lang.reflect.InvocationTargetException;

public class RoomSOAPConverter {

    public static Room convertRoomSOAPToRoom(RoomSOAP roomSOAP) throws RoomSOAPException {
        try {
            Room room = new Room();
            return copyRoomSOAPToRoom(room, roomSOAP);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RoomSOAPException("Cannot convert soap room to domain room.");
        }
    }

    private static Room copyRoomSOAPToRoom(Room room, RoomSOAP roomSOAP) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(room, roomSOAP);
        return room;
    }

    public static RoomSOAP convertRoomToRoomSOAP(Room room) throws RoomSOAPException {
        try {
            RoomSOAP roomSOAP = new RoomSOAP();
            return copyRoomToRoomSOAP(roomSOAP, room);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RoomSOAPException("Cannot convert domain room to soap room.");
        }
    }

    private static RoomSOAP copyRoomToRoomSOAP(RoomSOAP roomSOAP, Room room) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(roomSOAP, room);
        return roomSOAP;
    }

}
