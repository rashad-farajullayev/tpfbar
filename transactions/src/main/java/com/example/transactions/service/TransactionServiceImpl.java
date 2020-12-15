package com.example.transactions.service;

import com.example.transactions.client.NotificationClient;
import com.example.transactions.entity.TransactionEntity;
import com.example.transactions.model.Transaction;
import com.example.transactions.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    public static final String MESSAGE_TYPE_SUCCESS = "success";
    private TransactionRepository transactionRepository;
    private NotificationClient notificationClient;
    private String messageTemplate;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, NotificationClient notificationClient, @Value("${message.template}") String messageTemplate) {
        this.transactionRepository = transactionRepository;
        this.notificationClient = notificationClient;
        this.messageTemplate = messageTemplate;
    }

    @Async("asyncExecutor")
    @Override
    public CompletableFuture<Boolean> createTransaction(Transaction transaction, Authentication authentication) {

        log.info("job started execution. result will be posted to " + transaction.getHookUrl());

        var t = TransactionEntity.builder()
                .userId(transaction.getUserId())
                .stockId(transaction.getStockId())
                .maximumPrice(transaction.getMaximumPrice())
                .hookUrl(transaction.getHookUrl())
                .build();

        transactionRepository.save(t);


        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        executor.submit(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000 * 60 * 2);
                log.info("Mockup. Job is finished successfully. Posting the result to URL: " + transaction.getHookUrl());

                String message = String.format(messageTemplate, authentication.getName(), transaction.getHookUrl());
                notificationClient.sendNotification(authentication, MESSAGE_TYPE_SUCCESS, message);
                log.info("Notification send: " + message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("job failed");
            }
        });


        log.info("Job submitted for execution");

        return CompletableFuture.completedFuture(Boolean.valueOf(true));
    }
}
