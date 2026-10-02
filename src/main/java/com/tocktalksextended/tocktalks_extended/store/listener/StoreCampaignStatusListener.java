package com.tocktalksextended.tocktalks_extended.store.listener;

import com.tocktalksextended.tocktalks_extended.campaign.dto.CampaignStatusChangedEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

@Component
public class StoreCampaignStatusListener implements MessageListener {

    private final ObjectMapper objectMapper;

    public StoreCampaignStatusListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        CampaignStatusChangedEvent event = objectMapper.readValue(json, CampaignStatusChangedEvent.class);

        System.out.printf("[Store] 캠페인 %d: %s -> %s — 가게 화면에 보여줄 캠페인 요약을 갱신합니다%n",
                event.campaignId(), event.previousStatus(), event.newStatus());
    }
}