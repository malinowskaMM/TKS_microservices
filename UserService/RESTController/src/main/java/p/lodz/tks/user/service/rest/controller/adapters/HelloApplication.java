package p.lodz.tks.user.service.rest.controller.adapters;


import p.lodz.tks.user.service.repositories.adapters.repositories.UserRepository;
import p.lodz.tks.user.service.repositories.modelEnt.user.AccessLevelEnt;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
@ApplicationScoped
@DeclareRoles({"ADMIN", "MANAGER", "CLIENT", "NONE"})
@RolesAllowed({"ADMIN", "MANAGER", "CLIENT", "NONE"})
public class HelloApplication extends Application {
    @Inject
    UserRepository userRepository;

    @PostConstruct
    public void addAdmin() {
        userRepository.createAdmin("adminLogin", "adminPassword", AccessLevelEnt.ADMIN);
        userRepository.createManager("managerLogin", "managerPassword", AccessLevelEnt.MANAGER);
        userRepository.createClient("11111111111", "clientName", "clientLastName", "clientAddress", "clientLogin", "clientPassword", AccessLevelEnt.CLIENT);
    }
}