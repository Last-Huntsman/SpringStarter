package org.zuzukov.synthetichumancorestarter.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zuzukov.synthetichumancorestarter.audit.AuditAspect;
import org.zuzukov.synthetichumancorestarter.audit.AuditProperties;
import org.zuzukov.synthetichumancorestarter.commands.CommandProcessor;
import org.zuzukov.synthetichumancorestarter.commands.CommandQueue;
import org.zuzukov.synthetichumancorestarter.commands.CommandWorker;
import org.zuzukov.synthetichumancorestarter.error.GlobalErrorHandler;
import org.zuzukov.synthetichumancorestarter.metrics.AndroidMetrics;


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
    public GlobalErrorHandler globalExceptionHandler() {
        return new GlobalErrorHandler();
    }
    @Bean
    @ConditionalOnMissingBean
    public AndroidMetrics androidMetrics() {
        return new AndroidMetrics();
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandWorker commandWorker(
            CommandQueue queue,
            AndroidMetrics androidMetrics,
            MeterRegistry meterRegistry
    ) {
        return new CommandWorker(queue, androidMetrics, meterRegistry);
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
