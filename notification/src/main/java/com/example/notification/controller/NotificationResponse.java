package com.example.notification.controller;

import com.example.notification.model.NotificationChannel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationResponse {
    private NotificationChannel channel;
    private boolean successfullySent;
}