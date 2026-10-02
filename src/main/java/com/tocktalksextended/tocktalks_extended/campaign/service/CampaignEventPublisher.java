package com.tocktalksextended.tocktalks_extended.campaign.service;

import com.tocktalksextended.tocktalks_extended.campaign.dto.CampaignStatusChangedEvent;
import com.tocktalksextended.tocktalks_extended.global.config.PubSubConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class CampaignEventPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public CampaignEventPublisher(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(CampaignStatusChangedEvent event) {
        String json = objectMapper.writeValueAsString(event);
        redisTemplate.convertAndSend(PubSubConfig.CAMPAIGN_STATUS_CHANNEL, json);
    }
}