package com.example.auth.service;

import com.example.auth.component.JwtTokenManager;
import com.example.auth.model.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    JwtTokenManager jwtTokenManager;

    @Override
    public AuthToken generateToken(String userName) {
        return AuthToken.builder().accessToken(jwtTokenManager.generateToken(userName)).build();
    }

    @Override
    public String validate(String jwtToken) {
        if (!jwtTokenManager.validateToken(jwtToken))
            return null;

        return jwtTokenManager.getUsernameFromJWT(jwtToken);
    }
}
