package com.tocktalksextended.tocktalks_extended.price.controller;

import com.tocktalksextended.tocktalks_extended.price.dto.response.KisPriceResponse;
import com.tocktalksextended.tocktalks_extended.price.service.KisAuthService;
import com.tocktalksextended.tocktalks_extended.price.service.KisPriceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController {

    private final KisAuthService kisAuthService;
    private final KisPriceService kisPriceService;

    public PriceController(KisAuthService kisAuthService, KisPriceService kisPriceService) {
        this.kisAuthService = kisAuthService;
        this.kisPriceService = kisPriceService;
    }

    @GetMapping("/api/price/kis-token")
    public String getKisToken() {
        return kisAuthService.getAccessToken();
    }

    @GetMapping("/api/price/{stockCode}")
    public KisPriceResponse getCurrentPrice(@PathVariable String stockCode) {
        return kisPriceService.getCurrentPrice(stockCode);
    }
}