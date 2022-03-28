package com.example.redislock.service;

public interface RedisLockService {

    void redisLockTest();

    void runTask(int taskNo, long sleep);

}
