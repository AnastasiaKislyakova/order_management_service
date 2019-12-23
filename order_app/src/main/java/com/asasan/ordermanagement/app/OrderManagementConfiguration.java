package com.asasan.ordermanagement.app;

import com.asasan.ordermanagement.queue.ChangeStatusQueue;
import com.asasan.ordermanagement.queue.RabbitMQChangeStatusQueue;
import com.asasan.warehousemanagement.api.feign.WarehouseManagementServiceClient;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class OrderManagementConfiguration {

    @Bean
    public ChangeStatusQueue changeStatusQueue() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return new RabbitMQChangeStatusQueue(channel);
    }

    @Bean
    public WarehouseManagementServiceClient warehouseClient() {
        WarehouseManagementServiceClient client = Feign.builder()
                .encoder(new GsonEncoder())
                .decode404()
                .decoder(new GsonDecoder())
                .target(WarehouseManagementServiceClient.class, "http://localhost:8080/api/warehouse");
        return client;
    }
}
