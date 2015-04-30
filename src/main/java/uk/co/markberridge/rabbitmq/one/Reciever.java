package uk.co.markberridge.rabbitmq.one;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class Reciever implements Runnable {

    private final int n;
    private final static String QUEUE_NAME = "hello";

    public Reciever(int n) {
        this.n = n;
    }

    public static void main(String[] argv) throws Exception {
        (new Thread(new Reciever(1))).start();
        (new Thread(new Reciever(2))).start();
        (new Thread(new Reciever(3))).start();
        (new Thread(new Reciever(4))).start();
        (new Thread(new Reciever(5))).start();
    }

    public void run() {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.printf(" [%s] Waiting for messages. To exit press Ctrl+C%n", n);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(QUEUE_NAME, true, consumer);

            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                System.out.printf(" [%s] Received [%s]%n", n, message);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}