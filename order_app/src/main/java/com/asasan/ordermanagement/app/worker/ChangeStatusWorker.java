package com.asasan.ordermanagement.app.worker;

import com.asasan.ordermanagement.app.repository.OrderRepository;
import com.asasan.ordermanagement.app.service.OrderService;
import com.asasan.ordermanagement.queue.ChangeStatusQueue;
import com.asasan.ordermanagement.queue.ChangeStatusTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
public class ChangeStatusWorker implements ApplicationListener<ContextRefreshedEvent>, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(ChangeStatusWorker.class);
    private final ChangeStatusQueue queue;
    private final OrderService orderService;
    private Thread worker;

    public ChangeStatusWorker(ChangeStatusQueue queue, OrderService orderRepository) {
        this.queue = queue;
        this.orderService = orderRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        worker = new Thread(() -> {
            while (true) {
                try {
                    ChangeStatusTask task = queue.get();
                    if (task != null ) {
                        logger.info("Received task: " + task);
                        orderService.changeOrderStatus(task.getOrderId(), task.getOrderStatus());
                    }
                    Thread.sleep(Duration.ofSeconds(1).toMillis());
                } catch (InterruptedException| IOException e) {
                    e.printStackTrace();
                }
            }
        });
        worker.start();
    }

    @Override
    public void close() throws Exception {
        worker.interrupt();
    }
}
