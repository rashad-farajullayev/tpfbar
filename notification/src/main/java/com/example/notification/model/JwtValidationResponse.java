package com.example.notification.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtValidationResponse {
    String userName;
    boolean isValid;
    String emailAddress;
}