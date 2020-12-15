package com.example.transactions.model;

import lombok.*;

@Builder
@ToString
@Data
public class NotificationRequest {
    private String from;
    private String to;
    private String type;
    private String content;
    private String email;
}
