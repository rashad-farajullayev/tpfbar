package com.example.transactions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String userId;
    private Long stockId;
    private BigDecimal maximumPrice;
    private String hookUrl;
}
