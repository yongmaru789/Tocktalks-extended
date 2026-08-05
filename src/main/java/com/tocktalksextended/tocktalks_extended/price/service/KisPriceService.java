package com.tocktalksextended.tocktalks_extended.price.service;

import com.tocktalksextended.tocktalks_extended.price.config.KisApiProperties;
import com.tocktalksextended.tocktalks_extended.price.dto.response.KisPriceEnvelope;
import com.tocktalksextended.tocktalks_extended.price.dto.response.KisPriceResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Service
public class KisPriceService {

    private static final String TR_ID_INQUIRE_PRICE = "FHKST01010100";
    private static final String CACHE_KEY_PREFIX = "price:rest:";
    private static final Duration CACHE_TTL = Duration.ofSeconds(8);

    private final WebClient kisWebClient;
    private final KisApiProperties kisApiProperties;
    private final KisAuthService kisAuthService;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final KisRateLimiter kisRateLimiter;

    public KisPriceService(WebClient kisWebClient,
                           KisApiProperties kisApiProperties,
                           KisAuthService kisAuthService,
                           RedisTemplate<String, String> redisTemplate,
                           ObjectMapper objectMapper,
                           KisRateLimiter kisRateLimiter) {
        this.kisWebClient = kisWebClient;
        this.kisApiProperties = kisApiProperties;
        this.kisAuthService = kisAuthService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.kisRateLimiter = kisRateLimiter;
    }

    public KisPriceResponse getCurrentPrice(String stockCode) {
        String cacheKey = CACHE_KEY_PREFIX + stockCode;

        String cachedJson = redisTemplate.opsForValue().get(cacheKey);
        if (cachedJson != null) {
            return objectMapper.readValue(cachedJson, KisPriceResponse.class);
        }

        KisPriceResponse response = fetchFromKis(stockCode);

        String json = objectMapper.writeValueAsString(response);
        redisTemplate.opsForValue().set(cacheKey, json, CACHE_TTL);

        return response;
    }

    private KisPriceResponse fetchFromKis(String stockCode) {
        kisRateLimiter.acquire();

        KisPriceEnvelope envelope = kisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/uapi/domestic-stock/v1/quotations/inquire-price")
                        .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                        .queryParam("FID_INPUT_ISCD", stockCode)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kisAuthService.getAccessToken())
                .header("appkey", kisApiProperties.appKey())
                .header("appsecret", kisApiProperties.appSecret())
                .header("tr_id", TR_ID_INQUIRE_PRICE)
                .header("custtype", "P")
                .retrieve()
                .bodyToMono(KisPriceEnvelope.class)
                .block();

        return envelope.output();
    }
}