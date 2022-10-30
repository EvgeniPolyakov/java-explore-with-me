package ru.practicum.explorewithme.compilation.service;

import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.model.CompilationDto;
import ru.practicum.explorewithme.event.model.Event;

import java.util.List;

public interface CompilationsService {
    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto getCompilationDtoById(Long id);

    Compilation getCompilationById(Long id);

    CompilationDto add(Compilation compilation);

    CompilationDto update(Long compilationId, Long eventId, Event event);

    void delete(Long id);

    CompilationDto pin(Long id);

    CompilationDto unpin(Long id);

    void deleteEvent(Long compilationId, Long eventId, Event eventToRemove);
}
