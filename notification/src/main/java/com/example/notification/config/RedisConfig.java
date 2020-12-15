package com.example.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.database}")
    private int redisDatabase;

    @Value("${redis.password}")
    private String redisPassword;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setDatabase(redisDatabase);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(60));

        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());

        return jedisConFactory;
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
