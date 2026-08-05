package com.tocktalksextended.tocktalks_extended.price.service;

import org.springframework.stereotype.Component;

@Component
public class KisRateLimiter {

    private static final long INTERVAL_MS = 70L; // 초당 약 14건으로 제한

    private long lastCallTimeMillis = 0L;

    public synchronized void acquire() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastCallTimeMillis;
        long waitTime = INTERVAL_MS - elapsed;

        if (waitTime > 0) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        lastCallTimeMillis = System.currentTimeMillis();
    }
}