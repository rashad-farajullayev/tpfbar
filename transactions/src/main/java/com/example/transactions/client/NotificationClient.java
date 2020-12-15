package com.example.transactions.client;

import com.example.transactions.model.JwtValidationResponse;
import com.example.transactions.model.NotificationRequest;
import com.example.transactions.model.SecurityConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Component

public class NotificationClient {

    public static final String TRANSACTIONS = "Transactions";
    public static final String EMAIL_KEY = "email";

    @Value("${ms-notification.url}")
    private String msNotificationUrl;

    public boolean sendNotification(Authentication authentication, String type, String content) {

        var credentials = (Map<String, String>) authentication.getCredentials();

        var notifRequest = NotificationRequest.builder()
                .from(TRANSACTIONS)
                .to(authentication.getName())
                .type(type)
                .content(content)
                .email(credentials.get(EMAIL_KEY))
                .build();

        var headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(SecurityConstants.AUTHORIZATION_HEADER, SecurityConstants.BEARER_PREFIX + credentials.get(SecurityConstants.ACCESS_TOKEN_KEY));

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<NotificationRequest> request = new HttpEntity<>(notifRequest, headers);
        ResponseEntity<JwtValidationResponse> response = restTemplate
                .exchange(msNotificationUrl, HttpMethod.POST, request, JwtValidationResponse.class);

        return response.getStatusCode() == HttpStatus.ACCEPTED;
    }
}
