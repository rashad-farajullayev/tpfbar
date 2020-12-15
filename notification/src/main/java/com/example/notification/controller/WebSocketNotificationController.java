package com.example.notification.controller;

import com.example.notification.model.WebSocketNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketNotificationController {

    @Autowired
    private SimpMessagingTemplate webSocket;

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/notification")
    public WebSocketNotification newUser(@Payload WebSocketNotification webSocketNotification,
                                        SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", webSocketNotification.getSender());

        return webSocketNotification;
    }

}
