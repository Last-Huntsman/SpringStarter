package org.zuzukov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zuzukov.synthetichumancorestarter.commands.Command;
import org.zuzukov.synthetichumancorestarter.commands.CommandProcessor;

@Service
public class ServiceCommand {
    CommandProcessor commandProcessor;

    @Autowired
    public void setCommandProcessor(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }
    public void addCommand(Command command) {
        commandProcessor.process(command);
    }
}
