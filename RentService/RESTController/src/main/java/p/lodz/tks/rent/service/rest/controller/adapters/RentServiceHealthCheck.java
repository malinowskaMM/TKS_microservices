package p.lodz.tks.rent.service.rest.controller.adapters;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;

@Path("/status")
@RequestScoped
public class RentServiceHealthCheck implements HealthCheck {

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
        String TEST_URL = "https://localhost:8181/TKS-2023-RentRestApi/api/rooms/test";
        Response response = client.target(TEST_URL).request().get();

        // Sprawdź kod odpowiedzi
        int statusCode = response.getStatus();

        return statusCode == 200;
    }
}
