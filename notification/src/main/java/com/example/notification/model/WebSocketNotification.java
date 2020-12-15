package com.example.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketNotification {

    private String type;
    private String content;
    private String sender;
}
