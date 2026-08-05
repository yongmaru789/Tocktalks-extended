package com.tocktalksextended.tocktalks_extended.price.service;

import com.tocktalksextended.tocktalks_extended.price.config.KisApiProperties;
import com.tocktalksextended.tocktalks_extended.price.dto.response.KisTokenResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

@Service
public class KisAuthService {

    private static final String CACHE_KEY = "kis:access-token";
    private static final long EXPIRY_SAFETY_MARGIN_SECONDS = 60;

    private final WebClient kisWebClient;
    private final KisApiProperties kisApiProperties;
    private final RedisTemplate<String, String> redisTemplate;

    public KisAuthService(WebClient kisWebClient, KisApiProperties kisApiProperties, RedisTemplate<String, String> redisTemplate) {
        this.kisWebClient = kisWebClient;
        this.kisApiProperties = kisApiProperties;
        this.redisTemplate = redisTemplate;
    }

    public String getAccessToken() {
        String cachedToken = redisTemplate.opsForValue().get(CACHE_KEY);
        if (cachedToken != null) {
            return cachedToken;
        }

        KisTokenResponse response = fetchAccessTokenFromKis();

        long ttlSeconds = response.expiresIn() - EXPIRY_SAFETY_MARGIN_SECONDS;
        redisTemplate.opsForValue().set(CACHE_KEY, response.accessToken(), Duration.ofSeconds(ttlSeconds));

        return response.accessToken();
    }

    private KisTokenResponse fetchAccessTokenFromKis() {
        return kisWebClient.post()
                .uri("/oauth2/tokenP")
                .bodyValue(Map.of(
                        "grant_type", "client_credentials",
                        "appkey", kisApiProperties.appKey(),
                        "appsecret", kisApiProperties.appSecret()
                ))
                .retrieve()
                .bodyToMono(KisTokenResponse.class)
                .block();
    }
}