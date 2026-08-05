package com.tocktalksextended.tocktalks_extended.price.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class KisDistributedRateLimiter {

    private static final long INTERVAL_MS = 70L;
    private static final String RATE_LIMIT_KEY = "kis:rate-limiter:next-allowed-time";

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisScript<Long> rateLimiterScript;

    public KisDistributedRateLimiter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.rateLimiterScript = RedisScript.of(new ClassPathResource("scripts/kis-rate-limiter.lua"), Long.class);
    }

    public void acquire() {
        long now = System.currentTimeMillis();

        Long nextAllowedTime = redisTemplate.execute(
                rateLimiterScript,
                Collections.singletonList(RATE_LIMIT_KEY),
                String.valueOf(INTERVAL_MS),
                String.valueOf(now)
        );

        long waitTime = nextAllowedTime - now;
        if (waitTime > 0) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}