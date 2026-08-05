package com.tocktalksextended.tocktalks_extended.price.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KisPriceEnvelope(
        String rtCd,
        String msgCd,
        String msg1,
        KisPriceResponse output
) {
}