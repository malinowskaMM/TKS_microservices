package adapters;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;
import java.util.Formatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
public class RoomResourceAdapterTest {

    String exampleUUID;

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

        JSONObject authDto = new JSONObject();
        authDto.put("login", "adminLogin");
        authDto.put("password", "adminPassword");

        Response auth = RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(authDto.toJSONString()).when().
                post(CONTAINER_URL + "/api/login");

        token = auth.getBody().asString();

        JSONObject createRoomRequest = new JSONObject();
        createRoomRequest.put("roomNumber", 1);
        createRoomRequest.put("price", 120.0);
        createRoomRequest.put("roomCapacity", 2);

        exampleUUID = RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRoomRequest.toJSONString()).when().
                post(CONTAINER_URL + "/api/rooms").
                then().statusCode(200)
                .extract().path("uuid");

    }

    @Test
    public void shouldCreateRoom() {
        JSONObject createRoomRequest = new JSONObject();
        createRoomRequest.put("roomNumber", 1);
        createRoomRequest.put("price", 120.0);
        createRoomRequest.put("roomCapacity", 2);

        String uuid = RestAssured.given().
                header("Authorization", "Bearer " + token).
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRoomRequest.toJSONString()).when().
                post(CONTAINER_URL + "/api/rooms").
                then().statusCode(200)
                .body("roomNumber", equalTo(1))
                .body("price", equalTo(120.0F))
                .body("roomCapacity", equalTo(2))
                .extract().path("uuid");

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/rooms/"+uuid);

    }

    @Test
    public void shouldDeleteRoomWithGivenId() {

        assertThat(exampleUUID).isNotNull();

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/rooms/"+exampleUUID)
                .then().statusCode(200);

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                delete(CONTAINER_URL + "/api/rooms/"+exampleUUID)
                .then().statusCode(200);

        RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/rooms/"+exampleUUID)
                .then().statusCode(404);
    }

    @Test
    public void shouldUpdateRoom() {
        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/rooms/"+exampleUUID);

        JSONObject createRoomRequest = new JSONObject();
        createRoomRequest.put("roomNumber", 1);
        createRoomRequest.put("price", 1200.0);
        createRoomRequest.put("roomCapacity", 2);

        RestAssured.given().
                header("If-Match", response.getHeader("ETag")).
                header("Authorization", "Bearer " + token).
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRoomRequest.toJSONString()).when().
                put(CONTAINER_URL + "/api/rooms/"+exampleUUID).
                then().statusCode(200);

        response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/rooms/"+exampleUUID);
    }

    @Test
    public void shouldGetRoomWithGivenId() {

        assertThat(exampleUUID).isNotNull();

        Response response = RestAssured.given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().get(CONTAINER_URL + "/api/rooms/"+exampleUUID);
    }
}