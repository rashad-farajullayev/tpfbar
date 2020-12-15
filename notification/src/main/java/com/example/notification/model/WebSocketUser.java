package com.example.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketUser implements Principal {

    private String userName;
    private String emailAddress;

    @Override
    public String getName() {
        return userName;
    }
}
