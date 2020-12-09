package com.example.auth.service;

import com.example.auth.model.AuthToken;

public interface TokenService {

    AuthToken generateToken(String userId);

    String validate(String jwtToken);
}
