package org.zuzukov;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zuzukov.synthetichumancorestarter.commands.Command;


/**
 * Hello world!
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        var context =  SpringApplication.run(App.class, args);
        Command command = new Command();

    }
}
