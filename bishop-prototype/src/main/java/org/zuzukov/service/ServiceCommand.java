package org.zuzukov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zuzukov.synthetichumancorestarter.audit.WeylandWatchingYou;
import org.zuzukov.synthetichumancorestarter.commands.Command;
import org.zuzukov.synthetichumancorestarter.commands.CommandProcessor;

@Service
@RequiredArgsConstructor
public class ServiceCommand {
    @Autowired
    private final CommandProcessor commandProcessor;

    @WeylandWatchingYou
    public void addCommand(Command command) {
        commandProcessor.process(command);
    }
}
