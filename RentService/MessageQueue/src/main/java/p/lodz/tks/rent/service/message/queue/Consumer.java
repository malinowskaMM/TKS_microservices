package p.lodz.tks.rent.service.message.queue;

import com.nimbusds.jose.JOSEException;
import com.rabbitmq.client.*;
import lombok.extern.java.Log;
import org.json.simple.JSONObject;
import p.lodz.tks.rent.service.application.core.application.services.services.auth.JwsGenerator;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.User;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.admin.Admin;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.manager.Manager;
import p.lodz.tks.rent.service.user.UserUseCase;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.UUID;

@Log
@ApplicationScoped
@Startup
public class Consumer {
    @Inject
    private UserUseCase userUseCase;

    @Inject
    Publisher publisher;

    private static final String HOST_NAME = "localhost";
    private static final int PORT_NUMBER = 5672;
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    private static final String EXCHANGE_NAME = "users_exchange";
    private static final String EXCHANGE_TYPE = "topic";
    private static final String CREATE_USER_KEY = "user.create";
    private static final String UPDATE_ROUTING_KEY = "user.update";
    private static final String DELETE_USER_KEY = "user.delete";

    private Channel channel;
    private String queueName;

    @PostConstruct
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(HOST_NAME);
            connectionFactory.setPort(PORT_NUMBER);
            connectionFactory.setUsername(USERNAME);
            connectionFactory.setPassword(PASSWORD);
            Connection connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
            channel.basicQos(1);
            queueName = channel.queueDeclare().getQueue();
            bindQueueWithChannel();
            getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            log.warning("RentService: Init error: " + e.getMessage());
        }
    }

    private void bindQueueWithChannel() throws IOException {
        channel.queueBind(queueName, EXCHANGE_NAME, CREATE_USER_KEY);
        channel.queueBind(queueName, EXCHANGE_NAME, UPDATE_ROUTING_KEY);
        channel.queueBind(queueName, EXCHANGE_NAME, DELETE_USER_KEY);
    }

    private void getMessage() throws IOException {
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
        });
    }

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        switch (delivery.getEnvelope().getRoutingKey()) {
            case CREATE_USER_KEY: {
                try {
                    createUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    if (e.getMessage().contains("Login")) {
                        log.info("RentService: " + e.getMessage());
                    } else {
                        String login = getUserLogin(delivery);
                        log.info("RentService: Error creating user: " + login + ", sending remove message");
                        publisher.deleteUser(login);
                    }
                }
                break;
            }
            case UPDATE_ROUTING_KEY: {
                try {
                    updateUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    log.info("RentService: There was an error updating the user");
                }
                break;
            }
            case DELETE_USER_KEY: {
                try {
                    removeUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    log.info("RentService: There was an error deleting the user");
                }
            }
            default: {
                break;
            }
        }
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    };

    private void createUser(String message) {
        log.info("RentService: Attempting to create user");
        User user = prepareUser(message);
        if (user != null) {
            if (user instanceof Admin) {
                userUseCase.registerAdmin(user.getUuid(), user.getLogin(), user.getPassword());
            } else if (user instanceof Manager) {
                userUseCase.registerManager(user.getUuid(), user.getLogin(), user.getPassword());
            } else if (user instanceof Client) {
                userUseCase.registerClient(
                        user.getUuid(),
                        ((Client) user).getFirstName(),
                        ((Client) user).getLastName(),
                        ((Client) user).getPersonalId(),
                        ((Client) user).getAddress(),
                        user.getLogin(),
                        user.getPassword()
                );
            }
            log.info("RentService: User " + user.getLogin() + " has been added");
        } else {
            log.info("RentService: There was an error adding the user");
        }
    }

    private void updateUser(String message) throws ParseException, JOSEException {
        log.info("RentService: Attempting to update user");
        User user = prepareUser(message);
        if (user != null) {
            if (user instanceof Client) {
                userUseCase.updateUser(
                        user.getUuid(),
                        ((Client) user).getFirstName(),
                        ((Client) user).getLastName(),
                        ((Client) user).getAddress(),
                        user.getLogin(),
                        user.getPassword(),
                        user.getAccessLevel()
                );
                log.info("RentService: Client " + user.getLogin() + "has been updated");
            } else {
                userUseCase.updateUser(
                        user.getUuid(),
                        null,
                        null,
                        null,
                        user.getLogin(),
                        user.getPassword(),
                        user.getAccessLevel()
                );
                log.info("RentService: User " + user.getLogin() + "has been updated");
            }

        } else {
            log.info("RentService: There was an error updating the user");
        }
    }

    private void removeUser(String message) {
        log.info("RentService: Removing user " + message);
        userUseCase.deleteUserByLogin(message);
    }

    private String getUserLogin(Delivery delivery) {
        JsonReader reader = Json
                .createReader(new StringReader(new String(delivery.getBody(), StandardCharsets.UTF_8)));
        JsonObject jsonObject = reader.readObject();
        return jsonObject.getString("login");
    }

    private User prepareUser(String message) {
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = reader.readObject();

        String accessLevel = jsonObject.getString("accessLevel").toLowerCase();

        switch (accessLevel) {
            case "admin" -> {
                if (jsonObject.containsKey("uuid")){
                    return new Admin(
                            UUID.fromString(jsonObject.getString("uuid")),
                            jsonObject.getString("login"),
                            jsonObject.getString("password"),
                            AccessLevel.ADMIN
                    );
                }

                return new Admin(
                        jsonObject.getString("login"),
                        jsonObject.getString("password"),
                        AccessLevel.ADMIN
                );

            }
            case "manager" -> {
                if (jsonObject.containsKey("uuid")) {
                    return new Manager(
                            UUID.fromString(jsonObject.getString("uuid")),
                            jsonObject.getString("login"),
                            jsonObject.getString("password"),
                            AccessLevel.MANAGER
                    );
                }

                return new Manager(
                    jsonObject.getString("login"),
                    jsonObject.getString("password"),
                    AccessLevel.MANAGER
                );
            }
            case "client" -> {
                if (jsonObject.containsKey("uuid")) {
                    return new Client(
                            UUID.fromString(jsonObject.getString("uuid")),
                            jsonObject.getString("personalId"),
                            jsonObject.getString("firstName"),
                            jsonObject.getString("lastName"),
                            jsonObject.getString("address"),
                            jsonObject.getString("login"),
                            jsonObject.getString("password"),
                            AccessLevel.CLIENT
                    );
                }

                return new Client(
                        jsonObject.getString("personalId"),
                        jsonObject.getString("firstName"),
                        jsonObject.getString("lastName"),
                        jsonObject.getString("address"),
                        jsonObject.getString("login"),
                        jsonObject.getString("password"),
                        AccessLevel.CLIENT
                );
            }
            default -> {
                return null;
            }
        }
    }
}
