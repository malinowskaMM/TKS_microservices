package p.lodz.tks.rent.service.rest.controller.adapters;


import p.lodz.tks.rent.service.application.core.application.services.services.auth.AuthIdentityStore;
import p.lodz.tks.rent.service.application.core.application.services.services.auth.JwtGenerator;
import p.lodz.tks.rent.service.rest.controller.dto.auth.AuthDto;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


@Path("/login")
@RequestScoped
public class AuthResourceAdapter {

    @Context
    private SecurityContext securityContext;

    @Inject
    AuthIdentityStore authIdentityStore;

    JwtGenerator jwtGenerator = new JwtGenerator();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"NONE"})
    public Response login(@NotNull AuthDto authDto) {
        UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredential(authDto.getLogin(), authDto.getPassword());
            CredentialValidationResult credentialValidationResult = authIdentityStore.validate(usernamePasswordCredential);

            if(credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
                String jwt = jwtGenerator.generateJWT(authDto.getLogin(), credentialValidationResult.getCallerGroups().iterator().next());
                return Response.ok(jwt).build();
            }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

}
