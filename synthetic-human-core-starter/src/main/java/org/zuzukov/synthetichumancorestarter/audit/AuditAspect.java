package org.zuzukov.synthetichumancorestarter.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@org.aspectj.lang.annotation.Aspect
public class AuditAspect {
    Logger logger = LoggerFactory.getLogger(AuditAspect.class);
    @Autowired
    AuditProperties auditProperties;

    @Around("@annotation(WeylandWatchingYou)")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object result = joinPoint.proceed();
        Object name = joinPoint.getSignature().getName();
        Object className = joinPoint.getTarget().getClass().getName();
        if (auditProperties.getAuditType() == AuditProperties.AuditType.CONSOLE) {
            logger.info("[AUDIT] Class:{} Method: {}, Args: {}, Result: {}", className, name, Arrays.toString(args), result==null?"":result.toString());
        } //TODO СДелать Kafka

        return result;
    }
}
