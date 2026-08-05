package com.tocktalksextended.tocktalks_extended.price.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PriceConfig {

    @Bean
    public WebClient kisWebClient(KisApiProperties kisApiProperties) {
        return WebClient.builder()
                .baseUrl(kisApiProperties.restBaseUrl())
                .build();
    }
}