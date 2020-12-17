package com.example.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Validated
public class NotificationRequest {
    private String from;

    @NotNull
    @Size(min = 1, max = 50, message = "Please specify username to whome you wonna send the message")
    private String to;

    @Max(value = 10)
    @Pattern(regexp = "[success|info|warning|error]")
    private String type;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String content;

    @Email(message = "Please specify email address to send the message if user is not online")
    private String email;
}
