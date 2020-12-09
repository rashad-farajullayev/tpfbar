package com.example.transactions.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

@Slf4j
public class JwtFilterImpl extends JwtFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, AuthenticationException {
            String header = request.getHeader(AUTHORIZATION_HEADER);

            if (header == null || !header.startsWith(BEARER_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            var jwtResponse = validateJwtToken(header.substring(BEARER_PREFIX.length()));

            if (jwtResponse.isValid()) {
                log.info("Authenticating as " + jwtResponse.getUserName());

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(jwtResponse.getUserName(), "", new ArrayList<>()));

            }
            else
                log.error("jwt verification failed");

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
