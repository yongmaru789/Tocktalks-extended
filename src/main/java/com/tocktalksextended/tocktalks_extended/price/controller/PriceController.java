package com.tocktalksextended.tocktalks_extended.price.controller;

import com.tocktalksextended.tocktalks_extended.price.dto.response.KisPriceResponse;
import com.tocktalksextended.tocktalks_extended.price.service.KisAuthService;
import com.tocktalksextended.tocktalks_extended.price.service.KisDistributedRateLimiter;
import com.tocktalksextended.tocktalks_extended.price.service.KisPriceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PriceController {

    private final KisAuthService kisAuthService;
    private final KisPriceService kisPriceService;
    private final KisDistributedRateLimiter kisDistributedRateLimiter;

    public PriceController(KisAuthService kisAuthService,
                           KisPriceService kisPriceService,
                           KisDistributedRateLimiter kisDistributedRateLimiter) {
        this.kisAuthService = kisAuthService;
        this.kisPriceService = kisPriceService;
        this.kisDistributedRateLimiter = kisDistributedRateLimiter;
    }

    @GetMapping("/api/price/kis-token")
    public String getKisToken() {
        return kisAuthService.getAccessToken();
    }

    @GetMapping("/api/price/{stockCode}")
    public KisPriceResponse getCurrentPrice(@PathVariable String stockCode) {
        return kisPriceService.getCurrentPrice(stockCode);
    }

    @GetMapping("/api/price/rate-limit-test")
    public List<Long> testDistributedRateLimiter() {
        List<Long> timestamps = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            kisDistributedRateLimiter.acquire();
            timestamps.add(System.currentTimeMillis());
        }
        return timestamps;
    }
}