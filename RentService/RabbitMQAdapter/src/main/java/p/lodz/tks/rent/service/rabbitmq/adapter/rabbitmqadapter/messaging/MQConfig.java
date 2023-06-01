package p.lodz.tks.rent.service.rabbitmq.adapter.rabbitmqadapter.messaging;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class MQConfig {

    private Connection connection;

    @Inject
    @ConfigProperty(name = "mq_host")
    private String mqHost;

    @Inject
    @ConfigProperty(name = "mq_port")
    private Integer mqPort;

    @Inject
    @ConfigProperty(name = "mq_username")
    private String mqUsername;

    @Inject
    @ConfigProperty(name = "mq_password")
    private String mqPassword;


    @Produces
    public Channel getChannel() throws IOException {
        if (connection == null) {
            System.out.println("Cannot get channel. Connection with RabbitMQ is not established");
            return null;
        }
        return connection.createChannel();
    }

    @PostConstruct
    void afterCreate() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost(mqHost);
        connectionFactory.setPort(mqPort);
        connectionFactory.setUsername(mqUsername);
        connectionFactory.setPassword(mqPassword);

        try {
            connection = connectionFactory.newConnection();
        } catch (IOException | TimeoutException ignored) {
            System.out.println("Error during establishing connection with RabbitMQ");
        }
    }

    @PreDestroy
    void beforeDestroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException ignored) {
                System.out.println("Error during closing connection with RabbitMQ");
            }
        }
    }
}
