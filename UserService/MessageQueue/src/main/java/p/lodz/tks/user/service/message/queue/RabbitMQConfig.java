package p.lodz.tks.user.service.message.queue;

public class RabbitMQConfig {
    public static final String HOST_NAME = "192.168.0.123";
    public static final int PORT_NUMBER = 5672;
    public static final String USERNAME = "guest";
    public static final String PASSWORD = "guest";
    public static final String EXCHANGE_NAME = "users_exchange";
    public static final String EXCHANGE_TYPE = "topic";
    public static final String CREATE_USER_KEY = "user.create";
    public static final String UPDATE_USER_KEY = "user.update";
    public static final String DELETE_USER_KEY = "user.delete";
}
