package org.zuzukov.synthetichumancorestarter.audit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


import static org.zuzukov.synthetichumancorestarter.audit.AuditProperties.AuditType.CONSOLE;


@Getter
@Setter
@ConfigurationProperties(prefix = "weyland.audit")
public class AuditProperties {
    public enum AuditType {
        CONSOLE, KAFKA

    }
    private AuditType auditType = CONSOLE;
    private String KafkaTopic = "weyland.audit";

}
