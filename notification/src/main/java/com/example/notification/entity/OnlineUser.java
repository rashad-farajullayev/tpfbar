package com.example.notification.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash("online_user")
@Data
@Builder
public class OnlineUser {

    @Id
    private String userName;
    private String email;

    @Builder.Default
    private LocalDateTime loginDateTime = LocalDateTime.now();
}