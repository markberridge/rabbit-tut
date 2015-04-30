package uk.co.markberridge.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.text.DateFormat;
import java.util.Date;

public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] arg)
            throws java.io.IOException, InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        for (int i = 1; i <= 1000000; i++) {
            String msg = String.format("%s - %s", i, DateFormat.getDateTimeInstance().format(new Date()));
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.printf(" [x] Sent [%s]%n", msg);
        }

        channel.close();
        connection.close();
    }
}