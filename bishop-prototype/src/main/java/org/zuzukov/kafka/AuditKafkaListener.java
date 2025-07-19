package org.zuzukov.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "weyland.audit", name = "audit-type", havingValue = "KAFKA")
public class AuditKafkaListener {
    Logger logger = LoggerFactory.getLogger(AuditKafkaListener.class);
    @KafkaListener(topics = "${weyland.audit.kafka-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        logger.info("[ AUDIT FROM KAFKA] {}", record.value());
    }
}
