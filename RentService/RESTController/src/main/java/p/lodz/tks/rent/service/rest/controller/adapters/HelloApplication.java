package p.lodz.tks.rent.service.rest.controller.adapters;

import p.lodz.tks.rent.service.repositories.adapters.repositories.RoomRepository;
import p.lodz.tks.rent.service.repositories.adapters.repositories.UserRepository;
import p.lodz.tks.rent.service.repositories.modelEnt.user.AccessLevelEnt;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.UUID;

@ApplicationPath("/api")
@ApplicationScoped
@DeclareRoles({"ADMIN", "MANAGER", "CLIENT", "NONE"})
@RolesAllowed({"ADMIN", "MANAGER", "CLIENT", "NONE"})
public class HelloApplication extends Application {
    @Inject
    UserRepository userRepository;

    @Inject
    RoomRepository roomRepository;

    @PostConstruct
    public void addAdmin() {
        roomRepository.createRoom(1, 100.0, 2);
        userRepository.createAdmin(UUID.randomUUID(), "adminLogin", "adminPassword", AccessLevelEnt.ADMIN);
        userRepository.createManager(UUID.randomUUID(), "managerLogin", "managerPassword", AccessLevelEnt.MANAGER);
        userRepository.createClient(UUID.randomUUID(), "11111111111", "clientName", "clientLastName", "clientAddress", "clientLogin", "clientPassword", AccessLevelEnt.CLIENT);
    }
}