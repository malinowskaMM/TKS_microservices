package p.lodz.tks.rent.service.message.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.java.Log;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Log
@ApplicationScoped
@Startup
public class Publisher {
    private static final String HOST_NAME = "localhost";
    private static final int PORT_NUMBER = 5672;
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    private static final String EXCHANGE_NAME = "users_exchange";
    private static final String EXCHANGE_TYPE = "topic";
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
            log.warning("RentService: Init error: " + e.getMessage());
        }
    }

    ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
        log.info("RentService: Message number: " + sequenceNumber + " successfully sent");
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
        log.warning("RentService: Message number: " + sequenceNumber + " failed");
        cleanOutstandingConfirms.handle(sequenceNumber, multiple);
    };

    @PreDestroy
    public void closeConnection() {
        try {
            connection.close();
        } catch (IOException e) {
            log.warning("RentService: Connection close error");
        }
    }

    public void deleteUser(String login) throws IOException {
        log.info("RentService: Sending remove message");
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        long sequenceNumber = channel.getNextPublishSeqNo();
        outstandingConfirms.put(sequenceNumber,login);
        channel.basicPublish(EXCHANGE_NAME, DELETE_USER_KEY,null, login.getBytes(StandardCharsets.UTF_8));
    }
}
