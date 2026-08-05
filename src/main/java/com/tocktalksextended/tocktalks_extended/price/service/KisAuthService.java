package com.tocktalksextended.tocktalks_extended.price.service;

import com.tocktalksextended.tocktalks_extended.price.config.KisApiProperties;
import com.tocktalksextended.tocktalks_extended.price.dto.response.KisTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class KisAuthService {

    private final WebClient kisWebClient;
    private final KisApiProperties kisApiProperties;

    public KisAuthService(WebClient kisWebClient, KisApiProperties kisApiProperties) {
        this.kisWebClient = kisWebClient;
        this.kisApiProperties = kisApiProperties;
    }

    public String getAccessToken() {
        KisTokenResponse response = kisWebClient.post()
                .uri("/oauth2/tokenP")
                .bodyValue(Map.of(
                        "grant_type", "client_credentials",
                        "appkey", kisApiProperties.appKey(),
                        "appsecret", kisApiProperties.appSecret()
                ))
                .retrieve()
                .bodyToMono(KisTokenResponse.class)
                .block();

        return response.accessToken();
    }
}