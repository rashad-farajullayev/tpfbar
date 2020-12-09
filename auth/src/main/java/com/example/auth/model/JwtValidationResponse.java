package com.example.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtValidationResponse {
    String userName;
    boolean isValid;
}