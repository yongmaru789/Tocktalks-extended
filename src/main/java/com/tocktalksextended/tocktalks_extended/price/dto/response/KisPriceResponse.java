package com.tocktalksextended.tocktalks_extended.price.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KisPriceResponse(
        String stckPrpr,   // 주식 현재가
        String prdyVrss,   // 전일 대비
        String prdyCtrt    // 전일 대비율(%)
) {
}