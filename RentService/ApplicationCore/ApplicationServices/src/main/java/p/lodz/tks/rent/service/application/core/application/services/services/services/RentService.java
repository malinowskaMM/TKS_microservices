package p.lodz.tks.rent.service.application.core.application.services.services.services;

import com.nimbusds.jose.JOSEException;
import org.json.simple.JSONObject;
import p.lodz.tks.rent.service.application.core.application.services.services.auth.JwsGenerator;
import p.lodz.tks.rent.service.application.core.domain.model.exceptions.*;
import p.lodz.tks.rent.service.application.core.domain.model.model.rent.Rent;
import p.lodz.tks.rent.service.application.core.domain.model.model.room.Room;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;
import p.lodz.tks.rent.service.application.ports.control.rent.CreateRentPort;
import p.lodz.tks.rent.service.application.ports.control.rent.DeleteRentPort;
import p.lodz.tks.rent.service.application.ports.control.rent.EndRentPort;
import p.lodz.tks.rent.service.application.ports.infrastructure.rent.GetRentPort;
import p.lodz.tks.rent.service.rent.RentUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.NotFoundException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RentService implements RentUseCase {

    @Inject
    private CreateRentPort createRentPort;

    @Inject
    private DeleteRentPort deleteRentPort;

    @Inject
    private EndRentPort endRentPort;

    @Inject
    private GetRentPort getRentPort;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Inject
    private UserService userService;

    @Inject
    private RoomService roomService;

    private final JwsGenerator jwsGenerator = new JwsGenerator();

    @Override
    public Rent rentRoom(String roomId, LocalDateTime beginTime, LocalDateTime endTime)
            throws RoomNotAvailable, RentValidationFailed, DateTimeValidationFailed {
        User user = userService.getUserFromServerContext();
        if (user != null && validator.validate(roomId).isEmpty()) {
            if (beginTime.isBefore(endTime)) {
                    final List<Rent> rents = getRentPort.getCurrentRentsByRoom(UUID.fromString(roomId), beginTime, endTime);
                    if (rents.isEmpty() && userService.getUserByIdInside(user.getUuid()).isActive()) {
                        return createRentPort.createRent(beginTime, endTime, user.getLogin(), roomId);
                    } else {
                        throw new RoomNotAvailable("Room is not available");
                    }
            } else {
                throw new DateTimeValidationFailed("Begin time of reservation is not earlier than end time of reservation");
            }
        } else {
            throw new RentValidationFailed("Cannot rent room");
        }
    }

    @Override
    public void endRoomRent(UUID id, String jws) throws RentWithGivenIdNotFound, JOSEException, ParseException {
        final Rent rent = getRentPort.getRentById(id);
        if(rent != null) {
            if (this.jwsGenerator.verify(jws)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", rent.getId().toString());
                String newJwt = this.jwsGenerator.generateJws(jsonObject.toString());
                if (newJwt.equals(jws)) {
                    endRentPort.endRent(id);
                }
            }
        }
        throw new RentWithGivenIdNotFound("Cannot find room with given id.");
    }

    @Override
    public List<Rent> getRents() {
        return getRentPort.getRents();
    }

    @Override
    public List<Rent> getPastRents() {
        return getRents().stream().filter(rent -> (rent.getBeginTime().isBefore(LocalDateTime.now()) && rent.getEndTime().isBefore(LocalDateTime.now()))).toList();
    }

    @Override
    public List<Rent> getCurrentRents() {
        return getRents().stream().filter(rent -> (rent.getBeginTime().isBefore(LocalDateTime.now()) && rent.getEndTime().isAfter(LocalDateTime.now()))).toList();
    }

    @Override
    public List<Rent> getPastRentsByClientId(String login) {
        userService.findClientsByLoginPart(login);
        return getPastRents().stream().filter(rent -> rent.getLogin().equals(login)).toList();
    }

    @Override
    public List<Rent> getCurrentRentsByClientId(String login) {
        userService.findClientsByLoginPart(login);
        return getCurrentRents().stream().filter(rent -> rent.getLogin().equals(login)).toList();
    }

    @Override
    public List<Rent> getPastRentsByRoomId(UUID roomId) {
        roomService.getRoomById(roomId);
        return getPastRents().stream().filter(rent -> rent.getRoomId().equals(roomId.toString())).toList();
    }

    @Override
    public List<Room> getCurrentFreeRooms() {
        return roomService.getAllRooms().stream().filter(room -> getCurrentRentsByRoomId(room.getUuid()).isEmpty()).toList();
    }

    @Override
    public List<Rent> getCurrentRentsByRoomId(UUID roomId) {
        roomService.getRoomById(roomId);
        return getCurrentRents().stream().filter(rent -> rent.getRoomId().equals(roomId.toString())).toList();
    }

    @Override
    public List<Rent> getRentsByClientId(String login) throws UserWithGivenIdNotFound {
        userService.findClientsByLoginPart(login);
        return getRentPort.getRentsByClient(login);
    }

    @Override
    public List<Rent> getRentsByRoomId(UUID roomId) throws RoomWithGivenIdNotFound {
        roomService.getRoomById(roomId);
        return getRentPort.getRentsByRoom(roomId);
    }

    @Override
    public List<Rent> getRentsByStartDate(LocalDateTime startDate) {
        return getRentPort.getRents().stream().filter(rent -> rent.getBeginTime().isEqual(startDate)).toList();
    }

    @Override
    public List<Rent> getRentsByEndDate(LocalDateTime endDate) {
        return getRentPort.getRents().stream().filter(rent -> rent.getEndTime().isEqual(endDate)).toList();
    }

    @Override
    public Rent getRent(UUID id) throws NotFoundException {
        final Rent rent = getRentPort.getRentById(id);
        if (rent == null) {
            throw new NotFoundException("Cannot find rent with given id");
        }
        return rent;
    }

    @Override
    public void removeRent(UUID rentId) throws RentWithGivenIdNotFound {
        Rent rent = getRent(rentId);
        if(rent.getEndTime().isAfter(LocalDateTime.now())) {
            deleteRentPort.removeRent(getRent(rentId).getId());
        }
    }

}
