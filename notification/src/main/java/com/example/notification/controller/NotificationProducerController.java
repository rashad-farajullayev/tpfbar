package com.example.notification.controller;

import com.example.notification.model.NotificationRequest;
import com.example.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationProducerController {

    @Autowired
    NotificationService notificationService;

    @PostMapping("/notify")
    public void testMessage (@RequestBody NotificationRequest message) {

        notificationService.sendNotification(message);
    }

}
