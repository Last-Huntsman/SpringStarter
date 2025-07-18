package org.zuzukov.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zuzukov.synthetichumancorestarter.commands.Command;
import org.zuzukov.synthetichumancorestarter.commands.CommandProcessor;


@RestController
@RequestMapping("/api/commands")
public class CommandRestController {
    
    @PostMapping
    public ResponseEntity<Void> setCommand(@RequestBody Command command) {
        System.out.println(command);
        return ResponseEntity.ok().build();
    }
}