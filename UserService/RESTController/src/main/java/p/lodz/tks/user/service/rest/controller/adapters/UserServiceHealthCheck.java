package p.lodz.tks.user.service.rest.controller.adapters;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

@Path("/status")
@RequestScoped
public class UserServiceHealthCheck implements HealthCheck {

    @Override
    @GET
    @Path("/checkHealth")
    public HealthCheckResponse call() {
        boolean isServiceUp = checkServiceStatus();
        if (isServiceUp) {
            return HealthCheckResponse.up("Service is up and running");
        } else {
            return HealthCheckResponse.down("Service is not available");
        }
    }

    private boolean checkServiceStatus() {
        // Utwórz klienta JAX-RS
        Client client = ClientBuilder.newClient();

        // Wykonaj żądanie GET na usługę sieciową
        String TEST_URL = "https://localhost:8181/TKS-2023-UserRestApi/api/users/ping";
        Response response = client.target(TEST_URL).request().get();

        // Sprawdź kod odpowiedzi
        int statusCode = response.getStatus();

        return statusCode == 200;
    }
}
