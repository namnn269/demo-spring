package com.namnn.springwebsocket.config;

import com.namnn.springwebsocket.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebSocketLister {

    final private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void listenAfterDisconnect(SessionDisconnectEvent disconnectEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
        String sender = (String) headerAccessor.getSessionAttributes().get("username");
        StompCommand command = headerAccessor.getCommand();
        System.out.format("disconnect: %s\n", sender);
        messagingTemplate.convertAndSend("/topic1/greetings", new MessageDto(sender, null, " has left"));
    }

    @EventListener
    public void listenWhenConnect(SessionConnectEvent connectEvent) {
        Object source = connectEvent.getSource();
        System.out.println("in connect event: " + source);
    }

    @EventListener
    public void listenWhenConnected(SessionConnectedEvent connectedEvent) {
        Object source = connectedEvent.getSource();
        System.out.println("connect successfully: " + source);
    }

}
