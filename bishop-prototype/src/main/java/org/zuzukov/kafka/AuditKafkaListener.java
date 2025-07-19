package org.zuzukov.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "weyland.audit", name = "audit-type", havingValue = "KAFKA")
public class AuditKafkaListener {

    @KafkaListener(topics = "${weyland.audit.kafka-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println("[ AUDIT FROM KAFKA] " + record.value());
    }
}
