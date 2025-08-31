package com.ecommerce.notification;

import com.ecommerce.notification.payload.OrderCreatedEvent;
import com.ecommerce.notification.payload.OrderStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderEventConsumer {

    //this method will keep on triggering and listening to order.queue
    //OrderCreatedEvent is data model coming from producer
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEvent orderCreatedEvent) {
        System.out.println("Received Order Event: "+orderCreatedEvent);

        long orderId = orderCreatedEvent.getOrderId();
        OrderStatus status = orderCreatedEvent.getStatus();

        System.out.println("OrderId : " + orderId + "\nOrderStatus: " + status);
    }
}
