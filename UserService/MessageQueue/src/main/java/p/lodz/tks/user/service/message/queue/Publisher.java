package p.lodz.tks.user.service.message.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.java.Log;
import p.lodz.tks.user.service.application.core.domain.model.model.user.User;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Log
@ApplicationScoped
public class Publisher {
    private static final String HOST_NAME = "localhost";
    private static final int PORT_NUMBER = 5672;
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    private static final String EXCHANGE_NAME = "users_exchange";
    private static final String EXCHANGE_TYPE = "topic";
    private static final String CREATE_USER_KEY = "user.create";
    private static final String UPDATE_USER_KEY = "user.update";
    private static final String DELETE_USER_KEY = "user.delete";
    private Connection connection;
    private Channel channel;
    ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();


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
            channel.confirmSelect();
            channel.addConfirmListener(cleanOutstandingConfirms, handleNack);
        } catch (Exception e) {
            e.printStackTrace();
            log.warning("UserService: Init error: " + e.getMessage());
        }
    }

    ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
        log.info("UserService: Message number: " + sequenceNumber + " successfully sent");
        if (multiple) {
            ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(
                    sequenceNumber, true
            );
            confirmed.clear();
        } else {
            outstandingConfirms.remove(sequenceNumber);
        }
    };

    ConfirmCallback handleNack = (sequenceNumber, multiple) -> {
        log.warning("UserService: Message number: " + sequenceNumber + " failed");
        cleanOutstandingConfirms.handle(sequenceNumber, multiple);
    };

    @PreDestroy
    public void closeConnection() {
        try {
            connection.close();
        } catch (IOException e) {
            log.warning("UserService: Connection close error");
        }
    }

    public void createUser(String json) throws IOException {
        log.info("UserService: Sending create message");
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        long sequenceNumber = channel.getNextPublishSeqNo();
        outstandingConfirms.put(sequenceNumber, json);
        channel.basicPublish(EXCHANGE_NAME, CREATE_USER_KEY, null, json.getBytes(StandardCharsets.UTF_8));
    }

    public void updateUser(String json) throws IOException {
        log.info("UserService: Sending update message");
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        long sequenceNumber = channel.getNextPublishSeqNo();
        outstandingConfirms.put(sequenceNumber, json);
        channel.basicPublish(EXCHANGE_NAME, UPDATE_USER_KEY, null, json.getBytes(StandardCharsets.UTF_8));
    }

    public void deleteUser(String login) throws IOException {
        log.info("UserService: Sending remove message");
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        long sequenceNumber = channel.getNextPublishSeqNo();
        outstandingConfirms.put(sequenceNumber, login);
        channel.basicPublish(EXCHANGE_NAME, DELETE_USER_KEY, null, login.getBytes(StandardCharsets.UTF_8));
    }

    public static class Serialization {
        private static JsonObjectBuilder prepareUser(User source) {
            return Json.createObjectBuilder()
                    .add("uuid", source.getUuid().toString())
                    .add("login", source.getLogin())
                    .add("password", source.getPassword())
                    .add("accessLevel", source.getAccessLevel().getAccessLevel());
        }

        public static String userToJsonString(User source) {
            return prepareUser(source).build().toString();
        }

        public static String clientToJsonString(
                User source,
                String personalId,
                String firstName,
                String lastName,
                String address
        ) {
            JsonObjectBuilder objectBuilder = prepareUser(source);
            return objectBuilder
                    .add("personalId", personalId)
                    .add("firstName", firstName)
                    .add("lastName", lastName)
                    .add("address", address)
                    .build().toString();
        }
    }
}
