package com.tocktalksextended.tocktalks_extended.campaign.controller;

import com.tocktalksextended.tocktalks_extended.campaign.service.CampaignService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping("/api/campaign/{campaignId}/status")
    public String changeStatus(@PathVariable Long campaignId,
                               @RequestParam String newStatus,
                               @RequestParam(defaultValue = "OWNER") String reason) {
        campaignService.changeStatus(campaignId, newStatus, reason);
        return "published";
    }
}