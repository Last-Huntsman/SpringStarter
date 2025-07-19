package org.zuzukov.synthetichumancorestarter.config;

import lombok.RequiredArgsConstructor;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.zuzukov.synthetichumancorestarter.audit.AuditProperties;

@Component
@RequiredArgsConstructor
public class AuditKafkaSender {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AuditProperties properties;

    public void send(String message) {
        kafkaTemplate.send(properties.getKafkaTopic(), message);
    }
}
