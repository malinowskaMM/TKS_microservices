package p.lodz.tks.user.service.message.queue;

import com.nimbusds.jose.JOSEException;
import com.rabbitmq.client.*;
import lombok.extern.java.Log;
import p.lodz.tks.user.service.application.core.domain.model.model.user.AccessLevel;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;
import p.lodz.tks.user.service.user.UserUseCase;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.UUID;

@Log
@ApplicationScoped
public class Consumer {
    @Inject
    private UserUseCase userUseCase;

    @Inject
    Publisher publisher;

    private static final String HOST_NAME = RabbitMQConfig.HOST_NAME;
    private static final int PORT_NUMBER = RabbitMQConfig.PORT_NUMBER;
    private static final String USERNAME = RabbitMQConfig.USERNAME;
    private static final String PASSWORD = RabbitMQConfig.PASSWORD;
    private static final String EXCHANGE_NAME = RabbitMQConfig.EXCHANGE_NAME;
    private static final String EXCHANGE_TYPE = RabbitMQConfig.EXCHANGE_TYPE;
    private static final String CREATE_USER_KEY = RabbitMQConfig.CREATE_USER_KEY;
    private static final String UPDATE_USER_KEY = RabbitMQConfig.UPDATE_USER_KEY;
    private static final String DELETE_USER_KEY = RabbitMQConfig.DELETE_USER_KEY;
    private Connection connection;
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
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
            channel.basicQos(1);
            queueName = channel.queueDeclare().getQueue();
            bindQueueWithChannel();
            getMessage();
        } catch (Exception e) {
            log.warning("UserService: Init error: " + e.getMessage());
        }
    }

    @PreDestroy
    public void closeConnection() {
        try {
            connection.close();
        } catch (IOException e) {
            log.warning("UserService: Connection close error");
        }
    }

    private void bindQueueWithChannel() throws IOException {
        channel.queueBind(queueName, EXCHANGE_NAME, CREATE_USER_KEY);
        channel.queueBind(queueName, EXCHANGE_NAME, DELETE_USER_KEY);
        channel.queueBind(queueName, EXCHANGE_NAME, UPDATE_USER_KEY);
    }

    private void getMessage() throws IOException {
        channel.basicConsume(queueName, false, deliverCallback, cancelCallback -> {
        });
    }

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        switch (delivery.getEnvelope().getRoutingKey()) {
            case CREATE_USER_KEY: {
                try {
                    createUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                }
                catch (Exception e) {
                    if (e.getMessage().contains("Login")) {
                        log.info("UserService: " + e.getMessage());
                    } else {
                        String login = getUserLogin(delivery);
                        log.info("UserService: Error creating user: " + login + ", sending remove message");
                        publisher.deleteUser(login);
                    }
                }
                break;
            }
            case UPDATE_USER_KEY: {
                try {
                    updateUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("UserService: There was an error updating the user");
                }
                break;
            }
            case DELETE_USER_KEY: {
                log.info("UserService: Received remove message");
                try {
                    deleteUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    log.info("UserService: There was an error deleting the user");
                }
            }
            default: {
                break;
            }
        }
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    };

    private void createUser(String message) {
        log.info("UserService: Attempting to create user");
        User user = prepareUser(message);
        userUseCase.addUser(user);
        log.info("UserService: User " + user.getLogin() + " has been added");
    }

    private void updateUser(String message) throws ParseException, JOSEException {
        log.info("UserService: Attempting to update user");
        User user = prepareUser(message);
        userUseCase.updateUser(user.getUuid(), user.getLogin(), user.getPassword(), user.getAccessLevel());
        log.info("UserService: User " + user.getLogin() + " has been updated");
    }

    private void deleteUser(String message) {
        log.info("UserService: Removing user " + message);
        userUseCase.deleteUserByLogin(message);
    }

    private User prepareUser(String message) {
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = reader.readObject();

        if (jsonObject.containsKey("uuid")){
            return new User(
                    UUID.fromString(jsonObject.getString("uuid")),
                    true,
                    jsonObject.getString("login"),
                    jsonObject.getString("password"),
                    AccessLevel.valueOf(jsonObject.getString("accessLevel"))
            );
        }

        return new User(
                true,
                jsonObject.getString("login"),
                jsonObject.getString("password"),
                AccessLevel.valueOf(jsonObject.getString("accessLevel"))
        );
    }

    private String getUserLogin(Delivery delivery) {
        JsonReader reader = Json.createReader(new StringReader(
                new String(delivery.getBody(), StandardCharsets.UTF_8)));
        JsonObject jsonObject = reader.readObject();
        return jsonObject.getString("login");
    }
}
