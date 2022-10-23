package ru.practicum.explorewithme.compilation.service;

import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.event.model.Event;

import java.util.List;

public interface CompilationsService {
    List<Compilation> getAll(Boolean pinned, int from, int size);

    Compilation getById(Long id);

    Compilation add(Compilation compilation);

    Compilation update(Long compilationId, Long eventId, Event event);

    void delete(Long id);

    Compilation pin(Long id);

    Compilation unpin(Long id);

    void deleteEvent(Long compilationId, Long eventId, Event eventToRemove);
}
