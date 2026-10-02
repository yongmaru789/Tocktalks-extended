package com.tocktalksextended.tocktalks_extended.adserving.listener;

import com.tocktalksextended.tocktalks_extended.campaign.dto.CampaignStatusChangedEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

@Component
public class AdServingCampaignStatusListener implements MessageListener {

    private final ObjectMapper objectMapper;

    public AdServingCampaignStatusListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        CampaignStatusChangedEvent event = objectMapper.readValue(json, CampaignStatusChangedEvent.class);

        System.out.printf("[AdServing] 캠페인 %d: %s -> %s (사유: %s) — 오늘 피드 후보 스냅샷의 서빙 상태를 갱신합니다%n",
                event.campaignId(), event.previousStatus(), event.newStatus(), event.reason());
    }
}