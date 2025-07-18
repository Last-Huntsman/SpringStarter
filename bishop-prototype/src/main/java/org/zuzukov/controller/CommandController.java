package org.zuzukov.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zuzukov.service.ServiceCommand;
import org.zuzukov.synthetichumancorestarter.commands.Command;

@RestController
@RequestMapping("/api/commands")
public class CommandController {
    @Autowired
    ServiceCommand serviceCommand;

    @PostMapping
    public ResponseEntity<String> postCommand(@Valid Command command) {
        serviceCommand.addCommand(command);
        return ResponseEntity.ok().body(command.toString());
    }


}
