package com.example.transactions.config.filter;

import com.example.transactions.model.JwtValidationResponse;
import com.example.transactions.model.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class JwtFilterImpl extends JwtFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, AuthenticationException {
        String header = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith(SecurityConstants.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        var accessToken = header.substring(SecurityConstants.BEARER_PREFIX.length());
        var jwtResponse = validateJwtToken(accessToken);

        if (jwtResponse.isValid()) {
            log.info("Authenticating as " + jwtResponse.getUserName());

            var credentials = new HashMap<String, String>();
            credentials.put(SecurityConstants.EMAIL_KEY, jwtResponse.getEmailAddress());
            credentials.put(SecurityConstants.ACCESS_TOKEN_KEY, accessToken);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(jwtResponse.getUserName(), credentials, new ArrayList<>()));

        } else
            log.error("JWT verification failed");

        filterChain.doFilter(request, response);
    }

    @Value("${ms-auth.url}")
    private String msAuthUrl;

    private JwtValidationResponse validateJwtToken(String jwt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(jwt);
        ResponseEntity<JwtValidationResponse> response = restTemplate
                .exchange(msAuthUrl, HttpMethod.POST, request, JwtValidationResponse.class);

        return response.getBody();
    }
}
