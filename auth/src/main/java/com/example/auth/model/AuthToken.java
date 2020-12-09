package com.example.auth.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthToken {
    private String accessToken;
    private String refreshToken;
}
