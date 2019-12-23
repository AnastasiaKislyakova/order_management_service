package com.asasan.ordermanagement.queue;

import com.asasan.ordermanagement.api.dto.OrderStatus;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RabbitMQChangeStatusQueue implements ChangeStatusQueue {
    private static final String QUEUE_NAME = "order_status";

    private Channel channel;

    public RabbitMQChangeStatusQueue(Channel channel) {
        this.channel = channel;
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(ChangeStatusTask task) throws IOException {
        String msg = task.getOrderId() + " " + task.getOrderStatus().toString();
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));

    }

    @Override
    public ChangeStatusTask get() throws IOException {
        GetResponse response = channel.basicGet(QUEUE_NAME, true);
        if (response == null) return null;
        String[] body = new String(response.getBody()).split(" ");
        int orderId = Integer.parseInt(body[0]);
        OrderStatus status = OrderStatus.valueOf(body[1]);
        return new ChangeStatusTask(orderId, status);
    }
}
