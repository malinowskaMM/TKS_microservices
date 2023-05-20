package p.lodz.tks.rent.service.rest.controller.adapters;

import p.lodz.tks.rent.service.repositories.adapters.repositories.RoomRepository;
import p.lodz.tks.rent.service.repositories.adapters.repositories.UserRepository;
import p.lodz.tks.rent.service.repositories.modelEnt.user.AccessLevelEnt;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

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
        userRepository.createAdmin("adminLogin", "adminPassword", AccessLevelEnt.ADMIN);
        userRepository.createManager("managerLogin", "managerPassword", AccessLevelEnt.MANAGER);
        userRepository.createClient("11111111111", "clientName", "clientLastName", "clientAddress", "clientLogin", "clientPassword", AccessLevelEnt.CLIENT);
    }
}