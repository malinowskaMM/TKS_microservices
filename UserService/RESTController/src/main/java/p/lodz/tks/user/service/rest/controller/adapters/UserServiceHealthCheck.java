package p.lodz.tks.user.service.rest.controller.adapters;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Path("/status")
@RequestScoped
public class UserServiceHealthCheck implements HealthCheck {

    @Override
    @GET
    @Path("/checkHealth")
    public HealthCheckResponse call() {
        boolean isServiceUp = checkGetUsersStatus() && checkServiceStatus();
        if (isServiceUp) {
            return HealthCheckResponse.named("getUsersCheck")
                    .up()
                    .withData("message", "Usługa sieciowa działa poprawnie")
                    .build();
        } else {
            return HealthCheckResponse.named("getUsersCheck")
                    .down()
                    .withData("message", "Usługa sieciowa jest niedostępna")
                    .build();
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

    private boolean checkGetUsersStatus() {
        try {
            // Wykonaj zapytanie GET do usługi sieciowej, np. API zwracającego obiekty Room
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(HttpRequest.newBuilder()
                            .uri(new URI("https://localhost:8181/TKS-2023-UserRestApi/api/login"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString("{\"login\":\"adminLogin\",\"password\":\"adminPassword\"}"))
                            .build(), HttpResponse.BodyHandlers.ofString());


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://localhost:8181/TKS-2023-UserRestApi/api/users"))
                    .GET()
                    .header("Authorization", "Bearer "+response.body())
                    .build();

            HttpResponse<String> response1 = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Sprawdź kod odpowiedzi HTTP
            int statusCode = response1.statusCode();
            if (statusCode == 200) {
                // Usługa działa poprawnie
                return true;
            } else {
                // Usługa nie działa poprawnie
                return false;
            }
        } catch (IOException | InterruptedException e) {
            // Błąd podczas wykonania żądania lub obsługi odpowiedzi
            return false;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
