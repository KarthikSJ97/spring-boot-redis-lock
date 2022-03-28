package com.example.redislock.service;

import lombok.extern.log4j.Log4j2;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class RedisLockServiceImpl implements RedisLockService {

    public static final String LOCK_KEY_NAME = "random_key";
    public static final int LOCK_WAIT_TIME_IN_SECONDS = 4;
    public static final int LOCK_TIMEOUT_IN_SECONDS = 6;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void redisLockTest() {
        CompletableFuture.runAsync(() -> runTask(1, 3000));
        CompletableFuture.runAsync(() -> runTask(2, 8000));
        CompletableFuture.runAsync(() -> runTask(3, 3000));
        CompletableFuture.runAsync(() -> runTask(4, 2000));
    }

    @Override
    public void runTask(int taskNo, long sleep) {

        RLock rLock = redissonClient.getLock(LOCK_KEY_NAME);
        boolean lockAcquired = false;
        try {
            log.info("Running task: {}", taskNo);

            lockAcquired = rLock.tryLock(LOCK_WAIT_TIME_IN_SECONDS, LOCK_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

            if (lockAcquired) {
                log.info("Lock acquired. Hence, sleeping for {} ms for executing task: {}", sleep, taskNo);
                Thread.sleep(sleep);
                log.info("Completed executing task: {}", taskNo);
            } else {
                log.error("Failed to acquire lock for task: {}", taskNo);
            }
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.error(ex.getMessage());
        } finally{
            if(lockAcquired && rLock.isLocked()) {
                rLock.unlock();
                log.info("Successfully released lock after executing task: {}", taskNo);
            } else if(lockAcquired) {
                log.info("Lock lease time has expired and hence the lock is automatically released for task: {}", taskNo);
            } else {
                log.info("Lock is acquired but not by the current thread and hence cannot release the lock for task: {}", taskNo);
            }
        }
    }
}
