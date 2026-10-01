package com.tocktalksextended.tocktalks_extended.global.config;

import com.tocktalksextended.tocktalks_extended.adserving.listener.AdServingCampaignStatusListener;
import com.tocktalksextended.tocktalks_extended.store.listener.StoreCampaignStatusListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class PubSubConfig {

    public static final String CAMPAIGN_STATUS_CHANNEL = "campaign:status-changed";

    @Bean
    public ChannelTopic campaignStatusTopic() {
        return new ChannelTopic(CAMPAIGN_STATUS_CHANNEL);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            AdServingCampaignStatusListener adServingListener,
            StoreCampaignStatusListener storeListener,
            ChannelTopic campaignStatusTopic) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(adServingListener, campaignStatusTopic);
        container.addMessageListener(storeListener, campaignStatusTopic);
        return container;
    }
}