package ru.practicum.explorewithme.compilation.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewCompilationDto {
    @NotBlank
    @Size(max = 255)
    private String title;
    private boolean pinned;
    private List<Long> events;
}
