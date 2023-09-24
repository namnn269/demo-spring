package com.namnn.springwebsocket.controller;

import com.namnn.springwebsocket.dto.MessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/public-chat")
    public void exchangePublicMessage(MessageDto message, StompHeaderAccessor headerAccessor) {
        System.out.println("in controller, public : " + message);
        if (headerAccessor.getSessionAttributes() == null)
            headerAccessor.setSessionAttributes(new HashMap<>());
        headerAccessor.getSessionAttributes().putIfAbsent("username", message.getSender());
        messagingTemplate.convertAndSend("/topic/public.group", message);
    }

    @MessageMapping("/private-chat")
    public void exchangePrivateMessage(MessageDto message) {
        System.out.println("in controller, private : " + message);
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/topic.private", message);
    }

}
