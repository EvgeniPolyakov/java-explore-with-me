package ru.practicum.explorewithme.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.explorewithme.event.model.Event;

import java.util.List;

@Data
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private String title;
    private boolean pinned;
    private List<Event> events;
}
