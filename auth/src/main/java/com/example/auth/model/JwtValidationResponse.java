package com.example.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtValidationResponse {
    String userName;
    boolean isValid;
    String emailAddress;
}