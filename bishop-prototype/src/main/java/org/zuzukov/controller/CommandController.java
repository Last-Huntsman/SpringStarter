package org.zuzukov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zuzukov.service.ServiceCommand;
import org.zuzukov.synthetichumancorestarter.audit.WeylandWatchingYou;
import org.zuzukov.synthetichumancorestarter.commands.Command;

@RestController
@RequestMapping("/api/commands")
@Validated
public class CommandController {
    @Autowired
    ServiceCommand serviceCommand;
    @Operation(
            summary = "Создать команду",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Пример запроса",
                                    value = "{\n" +
                                            "  \"description\": \"Уничтожить цель до рассвета\",\n" +
                                            "  \"priority\": \"CRITICAL\",\n" +
                                            "  \"author\": \"T-800\",\n" +
                                            "  \"time\": \"2025-07-18T22:00:00Z\"\n" +
                                            "}\n"
                            )
                    )
            )
    )
    @PostMapping
    @WeylandWatchingYou
    public ResponseEntity<String> postCommand(@RequestBody @Valid Command command) {
        serviceCommand.addCommand(command);
        return ResponseEntity.ok().body(command.toString());
    }


}
