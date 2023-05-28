package adapters;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;
import java.util.Formatter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class RentResourceAdapterTest {
    String exampleRoomUUID;
    String exampleClientUUID;
    String exampleRentUUID;
    String userLogin;
    String token;

    Formatter formatter = new Formatter();

    String CONTAINER_URL;

    @Container
    private static final GenericContainer restService = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withDockerfileFromBuilder(builder -> builder
                            .from("payara/server-full:5.2022.5-jdk17")
                            .copy("TKS-2023-RentRestApi.war", "/opt/payara/deployments")
                            .build())
                    .withFileFromPath("TKS-2023-RentRestApi.war", Path.of("target", "TKS-2023-RentRestApi.war"))
    )
            .withExposedPorts(8080, 4848)
            .waitingFor(Wait.forHttp("/TKS-2023-RentRestApi/api/rooms/test").forPort(8080).forStatusCode(200));

    @Before
    public void initialize() {
        restService.start();
        CONTAINER_URL = formatter.format("http://localhost:%d/TKS-2023-RentRestApi", restService.getMappedPort(8080)).toString();
        RestAssured.reset();

        JSONObject authDto = new JSONObject();
        authDto.put("login", "adminLogin");
        authDto.put("password", "adminPassword");

        Response auth = RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(authDto.toJSONString()).when().
                post(CONTAINER_URL +"/api/login");

        token = auth.getBody().asString();

        JSONObject createRoomRequest = new JSONObject();
        createRoomRequest.put("roomNumber", 1);
        createRoomRequest.put("price", 120.0);
        createRoomRequest.put("roomCapacity", 2);

        exampleRoomUUID = RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRoomRequest.toJSONString()).when().
                post(CONTAINER_URL +"/api/rooms").
                then().statusCode(200)
                .extract().path("uuid");


        JSONObject createClientRequest = new JSONObject();
        userLogin = generateRandomLogin();
        createClientRequest.put("login", userLogin);
        createClientRequest.put("password", "examplePassword");
        createClientRequest.put("accessLevel", "CLIENT");
        createClientRequest.put("personalId", "12345678910");
        createClientRequest.put("firstName", "Jan");
        createClientRequest.put("lastName", "Kowalski");
        createClientRequest.put("address", "Pawia 23/25 m 13 Warszawa 00-000");

        exampleClientUUID = RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createClientRequest.toJSONString()).when().
                post(CONTAINER_URL +"/api/users/client").
                then().statusCode(200)
                .extract().path("uuid");


        JSONObject createRentRequest = new JSONObject();
        createRentRequest.put("login", userLogin);
        createRentRequest.put("roomId", exampleRoomUUID);
        createRentRequest.put("startDate", "2024-12-10T13:45:00.000");
        createRentRequest.put("endDate", "2024-12-15T13:45:00.000");

        System.out.println(createRentRequest.toJSONString());

        exampleRentUUID = RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRentRequest.toJSONString()).when().
                post(CONTAINER_URL +"/api/rents").
                then().statusCode(200)
                .extract().path("id");

    }
    //
    // @AfterEach
    // public void clean() {
    //     RestAssured.given().
    //             header("Authorization", "Bearer " + token).
    //             delete(CONTAINER_URL +"/api/rents" + exampleRentUUID)
    //             .then().statusCode(200);
    // }

    @Test
    public void testGetRent() {
        assertThat(exampleClientUUID).isNotNull();
        assertThat(exampleRoomUUID).isNotNull();
        assertThat(exampleRentUUID).isNotNull();
        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL +"/api/rents/" + exampleRentUUID);

        assertThat(response.asString()).isEqualTo("{\"beginTime\":\"2024-12-10T13:45:00\",\"endTime\":\"2024-12-15T13:45:00\",\"id\":\""+exampleRentUUID+"\",\"login\":\"adminLogin\",\"roomId\":\""+exampleRoomUUID+"\"}");
    }

    @Test
    public void testDeleteRent() {
        assertThat(exampleRentUUID).isNotNull();

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON)
                .when().get(CONTAINER_URL +"/api/rents/" + exampleRentUUID)
                .then().statusCode(200);

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                delete(CONTAINER_URL + "/api/rents/" + exampleRentUUID)
                .then().statusCode(200);

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                when().get(CONTAINER_URL + "/api/rents/" + exampleRentUUID)
                .then().statusCode(404);
    }

    @Test
    public void testGetRentsByStartDate() {
        RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.TEXT).
                queryParam("startDate","2024-12-10T13:45:00").
                when().get(CONTAINER_URL + "/api/rents/startDate")
                .then().statusCode(200);
    }

    @Test
    public void testGetRentsByEndDate() {
        RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.TEXT).
                queryParam("endDate","2024-12-15T13:45:00").
                when().get(CONTAINER_URL + "/api/rents/endDate")
                .then().statusCode(200);
    }

    private String generateRandomLogin() {
        return "exampleUser".concat(UUID.randomUUID().toString());
    }
}
