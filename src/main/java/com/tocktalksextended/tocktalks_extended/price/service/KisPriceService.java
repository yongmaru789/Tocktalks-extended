package com.tocktalksextended.tocktalks_extended.price.service;

import com.tocktalksextended.tocktalks_extended.price.config.KisApiProperties;
import com.tocktalksextended.tocktalks_extended.price.dto.response.KisPriceEnvelope;
import com.tocktalksextended.tocktalks_extended.price.dto.response.KisPriceResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KisPriceService {

    private static final String TR_ID_INQUIRE_PRICE = "FHKST01010100";

    private final WebClient kisWebClient;
    private final KisApiProperties kisApiProperties;
    private final KisAuthService kisAuthService;

    public KisPriceService(WebClient kisWebClient, KisApiProperties kisApiProperties, KisAuthService kisAuthService) {
        this.kisWebClient = kisWebClient;
        this.kisApiProperties = kisApiProperties;
        this.kisAuthService = kisAuthService;
    }

    public KisPriceResponse getCurrentPrice(String stockCode) {
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