package uk.co.markberridge.rabbitmq.two;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Reciever {

    public static void main(String[] argv) throws Exception {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            int prefetchCount = 1;
            channel.basicQos(prefetchCount);
            boolean durable = true;
            channel.queueDeclare(Queue.TASK_QUEUE, durable, false, false, null);

            System.out.printf(" [*] Waiting for messages. To exit press Ctrl+C%n");

            QueueingConsumer consumer = new QueueingConsumer(channel);
            boolean autoAck = false;
            channel.basicConsume(Queue.TASK_QUEUE, autoAck, consumer);

            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());

                System.out.printf(" [x] Received [%s]%n", message);
                doWork(message);

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                System.out.printf(" [x] Processed [%s]%n", message);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}