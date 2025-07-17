package org.zuzukov.synthetichumancorestarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.zuzukov.synthetichumancorestarter.audit.AuditProperties;
import org.zuzukov.synthetichumancorestarter.command.Command;
import org.zuzukov.synthetichumancorestarter.command.CommandProcessor;
import org.zuzukov.synthetichumancorestarter.command.Priority;

@SpringBootApplication
@EnableConfigurationProperties(AuditProperties.class)
public class SyntheticHumanCoreStarterApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(SyntheticHumanCoreStarterApplication.class, args);
        Command command = new Command("dfsf1", Priority.COMMON, "fds", "dfs");
        Command command1 = new Command("dfsf2", Priority.COMMON, "fds", "dfs");
        Command command2 = new Command("dfsf3", Priority.COMMON, "fds", "dfs");
        Command command3 = new Command("dfsf4", Priority.COMMON, "fds", "dfs");
        Command command21 = new Command("dfsf5", Priority.COMMON, "fds", "dfs");
        Command command123 = new Command("dfsf6", Priority.COMMON, "fds", "dfs");
        Command command223 = new Command("dfsf7", Priority.COMMON, "fds", "dfs");
        Command command3132 = new Command("dfsf8", Priority.COMMON, "fds", "dfs");

        CommandProcessor commandProcessor = context.getBean(CommandProcessor.class);
        commandProcessor.process(command);
        commandProcessor.process(command1);
        commandProcessor.process(command2);
        commandProcessor.process(command3);
        commandProcessor.process(command21);
        commandProcessor.process(command123);
        commandProcessor.process(command223);
        commandProcessor.process(command3132);
    }

}
