package uk.co.markberridge.rabbitmq.two;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Queue {

    public static final String HELLO = "hello";
    public static final String TASK_QUEUE = "task_queue";
}
