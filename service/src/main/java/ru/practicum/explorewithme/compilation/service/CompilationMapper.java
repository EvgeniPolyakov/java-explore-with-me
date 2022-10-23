package ru.practicum.explorewithme.compilation.service;

import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.model.NewCompilationDto;
import ru.practicum.explorewithme.event.model.Event;

import java.util.List;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto compilationDto, List<Event> events) {
        return new Compilation(
                null,
                compilationDto.getTitle(),
                compilationDto.getPinned(),
                events
        );
    }
}
