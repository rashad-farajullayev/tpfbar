package com.example.transactions.service;

import com.example.transactions.model.Transaction;
import org.springframework.security.core.Authentication;

import java.util.concurrent.CompletableFuture;

public interface TransactionService {

    CompletableFuture<Boolean> createTransaction(Transaction transaction, Authentication authentication);
}
