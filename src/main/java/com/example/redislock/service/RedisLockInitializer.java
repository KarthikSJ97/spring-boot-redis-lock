package com.example.redislock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RedisLockInitializer {

    private final RedisLockService redisLockService;

    @Autowired
    public RedisLockInitializer(RedisLockService redisLockService) {
        this.redisLockService = redisLockService;
    }

    @PostConstruct
    public void setup() {
        redisLockService.redisLockTest();
    }

}
