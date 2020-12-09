package com.example.auth.service;

import com.example.auth.model.JwtValidationResponse;
import com.example.auth.model.LdapLoginRequest;
import com.example.auth.model.LdapLoginResponse;

public interface AuthService {

    LdapLoginResponse login (LdapLoginRequest request);

    JwtValidationResponse validate (String jwtToken);
}
