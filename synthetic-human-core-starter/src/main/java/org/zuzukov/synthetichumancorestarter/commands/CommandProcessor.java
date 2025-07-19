package org.zuzukov.synthetichumancorestarter.commands;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.zuzukov.synthetichumancorestarter.metrics.AndroidMetrics;

@Service
@Validated
public class CommandProcessor {

    @Autowired
    private CommandQueue commandQueue;


    @Autowired
    private AndroidMetrics androidMetrics;

    @Autowired
    private MeterRegistry meterRegistry;

    private static final Logger logger = LoggerFactory.getLogger(CommandProcessor.class);

    public void process(@Valid Command command) {

        if (command.getPriority() == Priority.CRITICAL) {
            logger.info("Выполняется команда: {}", command.toString());
            androidMetrics.taskCompletedBy(command.getAuthor(), meterRegistry);
        } else {
            commandQueue.push(command);

        }
    }
}
