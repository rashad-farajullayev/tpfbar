package com.example.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LdapLoginResponse {

    private boolean successfull;
    private AuthToken authToken;
}
