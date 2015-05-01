package uk.co.markberridge.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Send {

    public static void main(String[] arg) throws java.io.IOException, InterruptedException {
        //        send("First message.");
        //        send("Second message..");
        //        send("Third message...");
        //        send("Fourth message....");
        //        send("Fifth message.....");
        for (int i = 1; i <= 20; i++) {
            StringBuilder s = new StringBuilder("Message ");
            s.append(i);
            for (int j = 0; j < ((i - 1) % 5) + 1; j++) {
                s.append('.');
            }
            send(s.toString());
        }
    }

    public static void send(String message) throws java.io.IOException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        boolean durable = true;
        channel.queueDeclare(Queue.TASK_QUEUE, durable, false, false, null);

        channel.basicPublish("", Queue.TASK_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}