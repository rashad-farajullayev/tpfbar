package com.example.notification.config;

import com.example.notification.model.JwtValidationResponse;
import com.example.notification.model.WebSocketUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class UserInterceptor implements ChannelInterceptor {

    private String msAuthUrl;

    public UserInterceptor(String msAuthUrl) {
        this.msAuthUrl = msAuthUrl;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor
                = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message
                    .getHeaders()
                    .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

            if (raw instanceof Map) {
                Object token = ((Map) raw).get("accesstoken");

                if (!(token instanceof ArrayList))
                    return message;

                String accessToken = ((ArrayList) token).get(0).toString();
                log.info("web socket trying to connect with token " + accessToken);

                JwtValidationResponse jwtValidationResponse = validateJwtToken(accessToken);
                if (jwtValidationResponse.isValid())
                    accessor.setUser(new WebSocketUser(jwtValidationResponse.getUserName(), jwtValidationResponse.getEmailAddress()));

            }
        }
        return message;
    }



    private JwtValidationResponse validateJwtToken(String jwt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(jwt);

        ResponseEntity<JwtValidationResponse> response = null;

        try {
            response = restTemplate
                    .exchange(msAuthUrl, HttpMethod.POST, request, JwtValidationResponse.class);

            JwtValidationResponse r = response.getBody();
            log.info("Validated Access Token: " + r.toString());
            return r;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return JwtValidationResponse.builder().isValid(false).build();
    }
}
