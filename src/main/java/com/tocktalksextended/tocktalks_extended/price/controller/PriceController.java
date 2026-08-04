package com.tocktalksextended.tocktalks_extended.price.controller;

import com.tocktalksextended.tocktalks_extended.price.service.KisAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController {

    private final KisAuthService kisAuthService;

    public PriceController(KisAuthService kisAuthService) {
        this.kisAuthService = kisAuthService;
    }

    @GetMapping("/api/price/kis-token")
    public String getKisToken() {
        return kisAuthService.getAccessToken();
    }
}