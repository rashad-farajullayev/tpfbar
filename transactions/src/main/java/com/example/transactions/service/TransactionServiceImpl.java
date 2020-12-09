package com.example.transactions.service;

import com.example.transactions.entity.TransactionEntity;
import com.example.transactions.model.Transaction;
import com.example.transactions.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl (TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Async("asyncExecutor")
    @Override
    public CompletableFuture<Boolean> createTransaction(Transaction transaction) {
        log.info("job started execution. result will be posted to " + transaction.getHookUrl());

        var t = TransactionEntity.builder()
                .userId(transaction.getUserId())
                .stockId(transaction.getStockId())
                .maximumPrice(transaction.getMaximumPrice())
                .hookUrl(transaction.getHookUrl())
                .build();

        transactionRepository.save(t);

        try {
            TimeUnit.MILLISECONDS.sleep(1000 * 60 * 2);
            log.info("Mockup. Job is finished successfully. Posting the result to URL: " + transaction.getHookUrl());
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("job failed");
        }

        return CompletableFuture.completedFuture(Boolean.valueOf(true));
    }
}
