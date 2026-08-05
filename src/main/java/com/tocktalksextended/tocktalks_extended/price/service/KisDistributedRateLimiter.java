package com.tocktalksextended.tocktalks_extended.price.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KisDistributedRateLimiter {

    private static final long INTERVAL_MS = 70L;
    private static final String RATE_LIMIT_KEY = "kis:rate-limiter:next-allowed-time";

    // KEYS[1] = 다음 호출 가능 시각을 담은 키, ARGV[1] = 호출 간격(ms), ARGV[2] = 현재 시각(ms)
    // 지금 호출해도 되면 0을, 아니면 몇 ms 기다려야 하는지를 반환하면서 동시에 다음 슬롯을 예약(SET)까지 원자적으로 처리한다.
    private static final String ACQUIRE_SCRIPT = """
            local nextAllowedAt = tonumber(redis.call('GET', KEYS[1]) or '0')
            local interval = tonumber(ARGV[1])
            local now = tonumber(ARGV[2])
            local base = now
            if nextAllowedAt > now then
                base = nextAllowedAt
            end
            redis.call('SET', KEYS[1], base + interval, 'PX', interval * 10)
            return base - now
            """;

    private static final DefaultRedisScript<Long> SCRIPT = new DefaultRedisScript<>(ACQUIRE_SCRIPT, Long.class);

    private final RedisTemplate<String, String> redisTemplate;

    public KisDistributedRateLimiter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void acquire() {
        Long waitMs = redisTemplate.execute(
                SCRIPT,
                List.of(RATE_LIMIT_KEY),
                String.valueOf(INTERVAL_MS),
                String.valueOf(System.currentTimeMillis())
        );

        if (waitMs != null && waitMs > 0) {
            try {
                Thread.sleep(waitMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}