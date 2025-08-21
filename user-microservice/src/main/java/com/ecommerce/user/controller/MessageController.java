package com.ecommerce.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
