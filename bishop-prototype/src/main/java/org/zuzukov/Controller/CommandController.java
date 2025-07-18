package org.zuzukov.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zuzukov.synthetichumancorestarter.commands.Command;

@RestController
@RequestMapping("/api/commands")
@Tag(name = "Команды", description = "Управление командами для синтетика Bishop")
public class CommandController {

    @Operation(summary = "Отправить команду синтетику")
    @PostMapping
    public ResponseEntity<String> sendCommand( @Valid @RequestBody Command command) {
        System.out.println(command);
        return ResponseEntity.ok().body(command.toString());
    }

    @Operation(summary = "Проверка работоспособности")
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok().body("pong");

    }
}
