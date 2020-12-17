package com.example.notification.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Builder
public class NotificationRequest {

    @NotNull
    @Size(min = 1, max = 50, message = "Please specify author name of this message")
    private String from;

    @NotNull
    @Size(min = 1, max = 50, message = "Please specify username to whome you wonna send the message")
    private String to;

    @Size(min = 1, max = 10)
    private String type;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String content;

    @Email(message = "Please specify email address to send the message if user is not online")
    private String email;
}
