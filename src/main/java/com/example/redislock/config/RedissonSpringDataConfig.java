package com.example.redislock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonSpringDataConfig {

    public static final String REDIS_CONNECTION_PROTOCOL = "redis://";

    @Value("${redis.connection.url}")
    private String redisConnectionUrl;

    @Value("${redis.connection.port}")
    private String redisConnectionPort;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        String address = REDIS_CONNECTION_PROTOCOL+redisConnectionUrl+":"+redisConnectionPort;
        config.useSingleServer()
                .setAddress(address);
        return Redisson.create(config);
    }
    
}