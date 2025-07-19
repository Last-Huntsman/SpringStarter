package org.zuzukov.synthetichumancorestarter.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zuzukov.synthetichumancorestarter.validator.TImeValidator;

@NoArgsConstructor
@AllArgsConstructor
@Getter
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
    @TImeValidator
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







