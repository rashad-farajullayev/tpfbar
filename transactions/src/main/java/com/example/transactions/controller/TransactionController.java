package com.example.transactions.controller;

import com.example.transactions.model.Transaction;
import com.example.transactions.service.TransactionService;
import io.swagger.annotations.ApiParam;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.concurrent.ExecutionException;

@RestController
public class TransactionController {

    TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public boolean newTransaction(Transaction transaction, @ApiIgnore Authentication authentication) throws ExecutionException, InterruptedException {


        return transactionService.createTransaction(transaction, authentication).get().booleanValue();
    }
}
