package com.example.notification.config.filter;

import com.example.notification.model.JwtValidationResponse;
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

import static com.example.notification.model.SecurityConstants.AUTHORIZATION_HEADER;
import static com.example.notification.model.SecurityConstants.BEARER_PREFIX;

@Slf4j
public class JwtFilterImpl extends JwtFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, AuthenticationException {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = header.substring(BEARER_PREFIX.length());
        var jwtResponse = validateJwtToken(accessToken);

        if (jwtResponse.isValid()) {
            log.info("Authenticating as " + jwtResponse.getUserName());

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(jwtResponse.getUserName(), "", new ArrayList<>()));

        } else
            log.error("jwt verification failed. access token: " + accessToken);

        filterChain.doFilter(request, response);
    }

    @Value("${ms-auth.url.validate}")
    private String msAuthUrl;

    private JwtValidationResponse validateJwtToken(String jwt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(jwt);
        ResponseEntity<JwtValidationResponse> response = restTemplate
                .exchange(msAuthUrl, HttpMethod.POST, request, JwtValidationResponse.class);

        return response.getBody();
    }
}
