package org.zuzukov.synthetichumancorestarter.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Command {
    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    private Priority priority;

    @NotBlank
    @Size(max = 100)
    private String author;

    @NotBlank
//    @Pattern(regexp = "^[0-9\\-T:.Z]+$") // ISO 8601 //TODO Разобарться
    private String time;


    @Override
    public String toString() {
        return "Command{" +
                "description='" + description + '\'' +
                ", priority=" + priority +
                ", author='" + author + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}








