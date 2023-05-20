package p.lodz.tks.rent.service.rent;

import com.nimbusds.jose.JOSEException;
import p.lodz.tks.rent.service.application.core.domain.model.model.rent.Rent;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RentUseCase {
    Rent rentRoom(String roomId, LocalDateTime beginTime, LocalDateTime endTime);
    void endRoomRent(UUID id, String jws) throws JOSEException, ParseException;
    List<Rent> getRents();
    List<Rent> getPastRents();
    List<Rent> getCurrentRents();
    List<Rent> getPastRentsByClientId(String login);
    List<Rent> getCurrentRentsByClientId(String login);
    List<Rent> getPastRentsByRoomId(UUID roomId);
    List<Room> getCurrentFreeRooms();
    List<Rent> getCurrentRentsByRoomId(UUID roomId);
    List<Rent> getRentsByClientId(String login);
    List<Rent> getRentsByRoomId(UUID roomId);
    List<Rent> getRentsByStartDate(LocalDateTime startDate);
    List<Rent> getRentsByEndDate(LocalDateTime endDate);
    Rent getRent(UUID id);
    void removeRent(UUID rentId);
}
