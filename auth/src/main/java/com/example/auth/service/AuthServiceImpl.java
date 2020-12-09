package com.example.auth.service;

import com.example.auth.model.AuthToken;
import com.example.auth.model.JwtValidationResponse;
import com.example.auth.model.LdapLoginRequest;
import com.example.auth.model.LdapLoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    public static final String CN_PREFIX = "cn";
    private final LdapTemplate ldapTemplate;
    private final TokenService tokenService;

    @Autowired
    public AuthServiceImpl(LdapTemplate ldapTemplate, TokenService tokenService) {
        this.ldapTemplate = ldapTemplate;
        this.tokenService = tokenService;
    }

    @Override
    public LdapLoginResponse login(LdapLoginRequest request) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(CN_PREFIX, request.getUserName()));
        log.info("Checking to see if username and password is correct for user: " + filter.encode());
        boolean result = ldapTemplate.authenticate("", filter.encode(), request.getPassword());

        AuthToken token = null;

        if (result)
            token = tokenService.generateToken(request.getUserName());

        return LdapLoginResponse.builder().successfull(result).authToken(token).build();
    }

    @Override
    public JwtValidationResponse validate (String jwtToken) {

        String userName = tokenService.validate(jwtToken);
        if (userName == null)
            return new JwtValidationResponse(userName, false);

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(CN_PREFIX, userName));
        return new JwtValidationResponse(userName, true);
    }
}
