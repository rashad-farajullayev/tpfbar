package com.example.notification.component;

import com.example.notification.entity.OnlineUser;
import com.example.notification.model.WebSocketUser;
import com.example.notification.repository.OnlineUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;

@Component
@Slf4j
public class WebNotificationEventListener {

    @Autowired
    OnlineUserRepository onlineUserRepository;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

        WebSocketUser webSocketUser = (WebSocketUser)event.getUser();
        OnlineUser onlineUser = OnlineUser.builder()
                .userName(webSocketUser.getUserName())
                .email(webSocketUser.getEmailAddress())
                .loginDateTime(LocalDateTime.now())
                .build();

        log.info(String.format("User %s just connected", webSocketUser.getUserName()));

        if (!onlineUserRepository.findById(webSocketUser.getUserName()).isPresent())
            onlineUserRepository.save(onlineUser);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        WebSocketUser webSocketUser = (WebSocketUser)event.getUser();
        if (webSocketUser != null){
            log.info(String.format("User %s disconnected", webSocketUser.getUserName()));
        }

        onlineUserRepository.deleteById(webSocketUser.getUserName());
    }
}
