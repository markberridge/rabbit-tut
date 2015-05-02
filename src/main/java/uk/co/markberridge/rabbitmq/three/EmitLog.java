package uk.co.markberridge.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void  main(String [] args) throws IOException {
        for (int i = 1; i <= 20; i++) {
            StringBuilder s = new StringBuilder("Message ");
            s.append(i);
            for (int j = 0; j < ((i - 1) % 5) + 1; j++) {
                s.append('.');
            }
            send(s.toString());
        }
    }

    public static void send(String message) throws java.io.IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}

