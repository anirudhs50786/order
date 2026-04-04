package com.motocart.order_microservice.order.kafka.config;

import com.motocart.library.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public NewTopic inventoryUpdateEvent() {
        return TopicBuilder.name(KafkaTopics.INVENTORY_EVENTS)
                .replicas(1)
                .partitions(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "86400000")
                .build();
    }
}
