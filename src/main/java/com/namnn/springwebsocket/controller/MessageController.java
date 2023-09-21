package com.namnn.springwebsocket.controller;

import com.namnn.springwebsocket.dto.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/server/receive")
    @SendTo("/topic1/greetings")
    public Object exchangeMessage(Message message) {
        System.out.println("in controller: " + message);
        return message;
    }

}
