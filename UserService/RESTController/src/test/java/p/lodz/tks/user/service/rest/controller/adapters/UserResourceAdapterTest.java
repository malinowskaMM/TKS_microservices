package p.lodz.tks.user.service.rest.controller.adapters;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;
import java.util.Formatter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class UserResourceAdapterTest {
    static final Logger LOGGER = LoggerFactory.getLogger(UserResourceAdapterTest.class);

    String exampleUUID;
    String adminExampleUUID;
    String managerExampleUUID;
    String uniqueManagerLogin;
    String uniqueClientLogin;
    String uniqueAdminLogin;
    String token;
    Formatter formatter = new Formatter();
    String CONTAINER_URL;

    @Container
    private static final GenericContainer userService = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withDockerfileFromBuilder(builder -> builder
                            .from("payara/server-full:5.2022.5-jdk17")
                            .copy("TKS-2023-UserRestApi.war", "/opt/payara/deployments")
                            .build())
                    .withFileFromPath("TKS-2023-UserRestApi.war", Path.of("target", "TKS-2023-UserRestApi.war"))
    )
            .withExposedPorts(8080, 4848)
            .withLogConsumer(new Slf4jLogConsumer(LOGGER))
            .waitingFor(Wait.forHttp("/TKS-2023-UserRestApi/api/users/ping").forPort(8080).forStatusCode(200));

    @Before
    public void initialize() {
        userService.start();
        CONTAINER_URL = formatter.format("http://localhost:%d/TKS-2023-UserRestApi", userService.getMappedPort(8080)).toString();

        JSONObject authDto = new JSONObject();
        authDto.put("login", "adminLogin");
        authDto.put("password", "adminPassword");

        Response auth = RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(authDto.toJSONString()).when().
                post(CONTAINER_URL + "/api/login");

        token = auth.getBody().asString();

        JSONObject createClientRequest = new JSONObject();
        uniqueClientLogin = generateRandomLogin();
        createClientRequest.put("login", uniqueClientLogin);
        createClientRequest.put("password", "examplePassword");
        createClientRequest.put("accessLevel", "CLIENT");
        createClientRequest.put("personalId", "123456789");
        createClientRequest.put("firstName", "Janek");
        createClientRequest.put("lastName", "Kowalski");
        createClientRequest.put("address", "Warszawa");

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                body(createClientRequest.toJSONString()).when().
                post(CONTAINER_URL + "/api/users").
                then().statusCode(201);

        exampleUUID = RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json")
                .get(CONTAINER_URL + "/api/users/findByLogin/" + uniqueClientLogin)
                .then()
                .statusCode(200)
                .extract().path("uuid");


                JSONObject createAdminRequest = new JSONObject();
        uniqueAdminLogin = generateRandomLogin();
        createAdminRequest.put("login", uniqueAdminLogin);
        createAdminRequest.put("password", "examplePasswordAdmin");
        createAdminRequest.put("accessLevel", "ADMIN");

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                body(createAdminRequest.toJSONString()).when().
                post(CONTAINER_URL + "/api/users").
                then().statusCode(201);

        adminExampleUUID = RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json")
                .get(CONTAINER_URL + "/api/users/findByLogin/" + uniqueAdminLogin)
                .then()
                .statusCode(200)
                .extract().path("uuid");


        JSONObject createManagerRequest = new JSONObject();
        uniqueManagerLogin = generateRandomLogin();
        createManagerRequest.put("login", uniqueManagerLogin);
        createManagerRequest.put("password", "examplePasswordManager");
        createManagerRequest.put("accessLevel", "MANAGER");

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                body(createManagerRequest.toJSONString()).when().
                post(CONTAINER_URL + "/api/users").
                then().statusCode(201);

        managerExampleUUID = RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json")
                .get(CONTAINER_URL + "/api/users/findByLogin/" + uniqueManagerLogin)
                .then()
                .statusCode(200)
                .extract().path("uuid");
    }

    @Test
    public void shouldGetClientWithGivenId() {

        assertThat(exampleUUID).isNotNull();

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/" + exampleUUID);

        assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"CLIENT\",\"login\":\"" + uniqueClientLogin + "\",\"uuid\":\"" + exampleUUID + "\"}");
    }

    @Test
    public void shouldGetAdminWithGivenId() {

        assertThat(adminExampleUUID).isNotNull();

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/" + adminExampleUUID);

        assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"ADMIN\",\"login\":\"" + uniqueAdminLogin + "\",\"uuid\":\"" + adminExampleUUID + "\"}");

    }

    @Test
    public void shouldGetManagerWithGivenId() {

        assertThat(managerExampleUUID).isNotNull();

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/" + managerExampleUUID);

        assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"MANAGER\",\"login\":\"" + uniqueManagerLogin + "\",\"uuid\":\"" + managerExampleUUID + "\"}");
    }

    @Test
    public void shouldUpdateClientWithGivenId() {
        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/" + exampleUUID);

        assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"CLIENT\",\"login\":\"" + uniqueClientLogin + "\",\"uuid\":\"" + exampleUUID + "\"}");

        JSONObject changeClientRequest = new JSONObject();
        changeClientRequest.put("login", "exampleUser");
        changeClientRequest.put("password", "examplePassword");
        changeClientRequest.put("accessLevel", "CLIENT");
        changeClientRequest.put("personalId", "12345678910");
        changeClientRequest.put("firstName", "Jan");
        changeClientRequest.put("lastName", "Nowak");
        changeClientRequest.put("address", "Pawia 23/25 m 13 Warszawa 00-000");

        RestAssured.given().contentType(ContentType.JSON).
                header("If-Match", response.getHeader("ETag")).
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                body(changeClientRequest).
                when().put(CONTAINER_URL + "/api/users/" + exampleUUID);

        Response response2 = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/" + exampleUUID);

        assertThat(response2.statusCode()).isEqualTo(200);
    }

    @Test
    public void shouldDeleteClientWithGivenId() {
        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/" + exampleUUID);

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                when().delete(CONTAINER_URL + "/api/users/" + exampleUUID);

        Response response1 = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/" + exampleUUID);

    }

    @Test
    public void shouldCreateClient() {
        JSONObject createClientRequest = new JSONObject();
        String inTestUniqueClientLogin = generateRandomLogin();
        createClientRequest.put("login", inTestUniqueClientLogin);
        createClientRequest.put("password", "examplePassword");
        createClientRequest.put("accessLevel", "CLIENT");
        createClientRequest.put("personalId", "12345678910");
        createClientRequest.put("firstName", "Jan");
        createClientRequest.put("lastName", "Kowalski");
        createClientRequest.put("address", "Pawia 23/25 m 13 Warszawa 00-000");

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                body(createClientRequest.toJSONString()).when().
                post(CONTAINER_URL + "/api/users").
                then().statusCode(201);

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/findByLogin/" + inTestUniqueClientLogin)
                .then().statusCode(200);
    }

    @Test
    public void shouldCreateManager() {
        JSONObject createManagerRequest = new JSONObject();
        String inTestUniqueManagerLogin = generateRandomLogin();
        createManagerRequest.put("login", inTestUniqueManagerLogin);
        createManagerRequest.put("password", "examplePassword");
        createManagerRequest.put("accessLevel", "MANAGER");

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                body(createManagerRequest.toJSONString()).when().
                post(CONTAINER_URL + "/api/users").
                then().statusCode(201);

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/findByLogin/" + inTestUniqueManagerLogin);

        assertThat(response.asString()).startsWith("{\"accessLevel\":\"MANAGER\",\"login\":\"" + inTestUniqueManagerLogin + "\",\"uuid\":\"");
    }

    @Test
    public void shouldUpdateManager() {
        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/" + managerExampleUUID);

        JSONObject updateManagerRequest = new JSONObject();
        String inTestUniqueManagerLogin = generateRandomLogin();
        updateManagerRequest.put("login", inTestUniqueManagerLogin);
        updateManagerRequest.put("password", "examplePasswordManagerUpdate");
        updateManagerRequest.put("accessLevel", "MANAGER");

        Response response1 = RestAssured.given().contentType(ContentType.JSON).
                header("If-Match", response.getHeader("ETag")).
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                body(updateManagerRequest).
                when().put(CONTAINER_URL + "/api/users/" + managerExampleUUID);

        assertThat(response1.statusCode()).isEqualTo(200);

    }


    @Test
    public void shouldCreateAdmin() {
        JSONObject createAdminRequest = new JSONObject();
        String inTestUniqueAdminLogin = generateRandomLogin();
        createAdminRequest.put("login", inTestUniqueAdminLogin);
        createAdminRequest.put("password", "examplePassword");
        createAdminRequest.put("accessLevel", "ADMIN");

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                body(createAdminRequest.toJSONString()).when().
                post(CONTAINER_URL + "/api/users").
                then().statusCode(201);

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/findByLogin/" + inTestUniqueAdminLogin);

        assertThat(response.asString()).startsWith("{\"accessLevel\":\"ADMIN\",\"login\":\"" + inTestUniqueAdminLogin + "\",\"uuid\":\"");
    }

    @Test
    public void shouldUpdateAdmin() {
        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/" + adminExampleUUID);

        JSONObject updateAdminRequest = new JSONObject();
        String inTestUniqueAdminLogin = generateRandomLogin();
        updateAdminRequest.put("login", inTestUniqueAdminLogin);
        updateAdminRequest.put("password", "examplePasswordAdminUpdate");
        updateAdminRequest.put("accessLevel", "ADMIN");

        Response response1 = RestAssured.given().contentType(ContentType.JSON).
                header("If-Match", response.getHeader("ETag")).
                header("Authorization", "Bearer " + token).
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                body(updateAdminRequest).
                when().put(CONTAINER_URL + "/api/users/" + adminExampleUUID);

        assertThat(response1.statusCode()).isEqualTo(200);

    }

    @Test
    public void shouldActivateClientWithGivenId() {
        assertThat(exampleUUID).isNotNull();

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/users/" + exampleUUID);

        Response response1 = RestAssured.given().
                header("If-Match", response.getHeader("ETag")).
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().put(CONTAINER_URL + "/api/users/client/activate/" + exampleUUID);

        assertThat(response1.statusCode()).isEqualTo(200);

    }

    private String generateRandomLogin() {
        return "exampleUser".concat(UUID.randomUUID().toString());
    }
}