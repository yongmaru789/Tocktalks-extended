package com.tocktalksextended.tocktalks_extended.campaign.service;

import com.tocktalksextended.tocktalks_extended.campaign.dto.CampaignStatusChangedEvent;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CampaignService {

    private final Map<Long, String> campaignStatuses = new ConcurrentHashMap<>();
    private final CampaignEventPublisher eventPublisher;

    public CampaignService(CampaignEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void changeStatus(Long campaignId, String newStatus, String reason) {
        String previousStatus = campaignStatuses.getOrDefault(campaignId, "ACTIVE");
        campaignStatuses.put(campaignId, newStatus);

        eventPublisher.publish(new CampaignStatusChangedEvent(
                campaignId, previousStatus, newStatus, reason, System.currentTimeMillis()
        ));
    }
}