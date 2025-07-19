package org.zuzukov.synthetichumancorestarter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zuzukov.synthetichumancorestarter.audit.AuditAspect;
import org.zuzukov.synthetichumancorestarter.audit.AuditProperties;
import org.zuzukov.synthetichumancorestarter.commands.CommandProcessor;
import org.zuzukov.synthetichumancorestarter.commands.CommandQueue;
import org.zuzukov.synthetichumancorestarter.commands.CommandWorker;
import org.zuzukov.synthetichumancorestarter.error.GlobalExceptionHandler;

@Configuration
@EnableConfigurationProperties(AuditProperties.class)
public class SyntheticCoreAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public CommandQueue commandQueue() {
        return new CommandQueue();
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandWorker commandWorker(CommandQueue queue) {
        return new CommandWorker(queue);
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandProcessor commandProcessor() {
        return new CommandProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }

}
