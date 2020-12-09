package com.example.auth.controller;

import com.example.auth.model.JwtValidationResponse;
import com.example.auth.model.LdapLoginRequest;
import com.example.auth.model.LdapLoginResponse;
import com.example.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LdapLoginResponse auth(@Valid LdapLoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/validate")
    public JwtValidationResponse auth(@RequestBody String jwtToken) {

        return authService.validate(jwtToken);

    }
}
