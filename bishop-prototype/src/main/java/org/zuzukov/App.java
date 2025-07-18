package org.zuzukov;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.zuzukov.synthetichumancorestarter.commands.Command;

import java.util.Arrays;


@SpringBootApplication(scanBasePackages = "org.zuzukov")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

