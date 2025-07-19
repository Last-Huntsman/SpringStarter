package org.zuzukov.synthetichumancorestarter.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zuzukov.synthetichumancorestarter.config.KafkaConfiguration;

import java.util.Arrays;

@Component
@org.aspectj.lang.annotation.Aspect
public class AuditAspect {
    Logger logger = LoggerFactory.getLogger(AuditAspect.class);
    @Autowired
    AuditProperties auditProperties;
    @Autowired
    KafkaConfiguration kafkaConfiguration;

    @Around("@annotation(WeylandWatchingYou)")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object result = joinPoint.proceed();
        Object name = joinPoint.getSignature().getName();
        Object className = joinPoint.getTarget().getClass().getName();
        String message = String.format("[AUDIT] Class: %s Method: %s, Args: %s, Result: %s",
                className,
                name,
                Arrays.toString(args),
                result == null ? "" : result.toString());

        if (auditProperties.getAuditType() == AuditProperties.AuditType.CONSOLE) {
            logger.info(message);
        } else if (auditProperties.getAuditType() == AuditProperties.AuditType.KAFKA) {
            kafkaConfiguration.send(message);

        }

        return result;
    }
}
