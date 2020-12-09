package com.example.transactions.config.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtValidationResponse {
    String userName;
    boolean isValid;
}
