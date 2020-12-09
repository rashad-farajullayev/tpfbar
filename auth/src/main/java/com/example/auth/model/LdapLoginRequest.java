package com.example.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class LdapLoginRequest {

    @NotBlank
    private String userName;

    @NotNull
    private String password;
}
