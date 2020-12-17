package com.example.notification.service;

import com.example.notification.controller.NotificationResponse;
import com.example.notification.model.NotificationChannel;
import com.example.notification.model.NotificationRequest;
import com.example.notification.model.WebSocketNotification;
import com.example.notification.repository.OnlineUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    OnlineUserRepository onlineUserRepository;

    public NotificationResponse sendNotification(NotificationRequest message) {

        if (onlineUserRepository.findById(message.getTo()).isPresent()) {

            log.info(String.format("User %s is online. Sending web socket notification", message.getTo()));

            try {
                simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/topic/notification",
                        new WebSocketNotification(message.getType(), message.getContent(), message.getFrom()));

                return NotificationResponse.builder()
                        .channel(NotificationChannel.WEB_SOCKET)
                        .successfullySent(true)
                        .build();
            }
            catch (MessagingException ex) {
                ex.printStackTrace();
                log.error(ex.getMessage());

                return NotificationResponse.builder()
                        .channel(NotificationChannel.NONE)
                        .successfullySent(false)
                        .build();
            }
        }
        else
        {
            log.info(String.format("User %s is NOT online. Sending E-mail notification to %s", message.getTo(), message.getEmail()));

            return NotificationResponse.builder()
                    .channel(NotificationChannel.EMAIL)
                    .successfullySent(true)
                    .build();
        }

    }
}
