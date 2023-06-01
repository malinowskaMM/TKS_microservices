package p.lodz.tks.rent.service.rabbitmq.adapter.rabbitmqadapter.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import p.lodz.tks.rent.service.application.core.domain.model.model.user.client.Client;
import p.lodz.tks.rent.service.rabbitmq.adapter.rabbitmqadapter.event.ClientEvent;
import p.lodz.tks.user.service.application.core.domain.model.exceptions.ClientValidationFailed;
import p.lodz.tks.user.service.user.UserUseCase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class CreateClientConsumer {

    @Inject
    private Channel channel;

    @Inject
    private UserUseCase userUseCase;

    @Inject
    @ConfigProperty(name = "user_queue_name", defaultValue = "USER_QUEUE")
    private String queueName;

    private void initConsumer() {
        if (channel == null) {
            System.out.println("Error during initializing consumer, connection is not established");
            return;
        }

        try {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicConsume(queueName, true, this::deliverCallback, consumerTag -> {
            });
        } catch (IOException ignored) {
            System.out.println("Error during connecting to queue");
        }
    }

    void deliverCallback(String consumerTag, Delivery delivery) {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        ClientEvent clientEvent;

        try (Jsonb jsonb = JsonbBuilder.create()) {
            clientEvent = jsonb.fromJson(message, ClientEvent.class);
        } catch (Exception e) {
            System.out.println("Invalid message format");
            return;
        }

        try {
            Client client = clientEvent.toClient();
            userUseCase.registerClient(client.getFirstName(),
                    client.getLastName(),
                    client.getPersonalId(),
                    client.getAddress(),
                    client.getLogin(),
                    client.getPassword());
        } catch (ClientValidationFailed e) {
            System.out.println("Error during saving client to database");
        }
    }

}
