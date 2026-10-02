package com.tocktalksextended.tocktalks_extended.campaign.dto;

public record CampaignStatusChangedEvent(
        Long campaignId,
        String previousStatus,
        String newStatus,
        String reason,
        long changedAtEpochMilli
) {
}