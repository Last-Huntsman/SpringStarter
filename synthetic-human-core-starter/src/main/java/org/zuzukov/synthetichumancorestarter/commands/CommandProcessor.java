package org.zuzukov.synthetichumancorestarter.commands;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CommandProcessor {
    @Autowired
    private CommandQueue commandQueue;

    private static final Logger logger = LoggerFactory.getLogger(CommandProcessor.class);

    public void process(@Valid Command command) {
        if (command.getPriority() == Priority.CRITICAL) logger.info("Выполняется команда: {}", command.toString());
        else {
            commandQueue.push(command);
        }
    }
}
