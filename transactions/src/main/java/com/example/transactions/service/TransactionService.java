package com.example.transactions.service;

import com.example.transactions.model.Transaction;

import java.util.concurrent.CompletableFuture;

public interface TransactionService {

    public CompletableFuture<Boolean> createTransaction (Transaction transaction);
}
