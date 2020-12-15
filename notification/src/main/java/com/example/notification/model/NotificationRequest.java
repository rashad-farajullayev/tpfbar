package com.example.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class NotificationRequest {
    private String from;
    private String to;
    private String type;
    private String content;
    private String email;
}
