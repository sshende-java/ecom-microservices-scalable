package com.ecommerce.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RefreshScope bean will be re-initialized, will pick up updated configuration values
//You can trigger a refresh by calling the /actuator/refresh on any service-endpoint as you ALL service are connected through spring-cloud-bus and rabbitMQ
//RabbitMQ will broadcast refresh event to all services

@RestController
@RefreshScope
public class MessageController {

    @Value("${app.message:xyz}")
    private String message;

    @Value("${app.centralisedMessage:pqr}")
    private String centralisedMessage;

    @GetMapping("/message")
    public String getMessage() {
        return message+" "+centralisedMessage;
    }

}
