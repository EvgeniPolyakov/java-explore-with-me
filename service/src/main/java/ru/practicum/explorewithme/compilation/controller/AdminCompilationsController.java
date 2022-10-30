package ru.practicum.explorewithme.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.model.CompilationDto;
import ru.practicum.explorewithme.compilation.model.NewCompilationDto;
import ru.practicum.explorewithme.compilation.service.CompilationMapper;
import ru.practicum.explorewithme.compilation.service.CompilationsService;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationsController {
    public static final String ID_PATH_VARIABLE_KEY = "id";
    public static final String EVENT_ID_PATH_VARIABLE_KEY = "eventId";

    private final CompilationsService compilationsService;
    private final EventService eventService;

    @PostMapping
    public CompilationDto add(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.info("Получен запрос POST по пути /admin/compilations: {}", compilationDto);
        List<Event> events = eventService.getCompilationEvents(compilationDto);
        Compilation compilation = CompilationMapper.toCompilation(compilationDto, events);
        return compilationsService.add(compilation);
    }

    @PatchMapping("/{id}/events/{eventId}")
    public CompilationDto addEvent(@PathVariable(ID_PATH_VARIABLE_KEY) Long compilationId,
                                   @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Получен запрос PATCH по пути /admin/compilations (id: {}, eventId: {})", compilationId, eventId);
        Event eventToAdd = eventService.getEventById(eventId);
        return compilationsService.update(compilationId, eventId, eventToAdd);
    }

    @PatchMapping("/{id}/pin")
    public CompilationDto pin(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос PATCH для прикрепления подборки с id: {}", id);
        return compilationsService.pin(id);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос DELETE по пути /admin/compilations/ по id {}", id);
        compilationsService.delete(id);
    }

    @DeleteMapping("/{id}/events/{eventId}")
    public void removeEvent(@PathVariable(ID_PATH_VARIABLE_KEY) Long compilationId,
                            @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Получен запрос DELETE по пути /admin/compilations (id: {}, eventId: {})", compilationId, eventId);
        Event eventToRemove = eventService.getEventById(eventId);
        compilationsService.deleteEvent(compilationId, eventId, eventToRemove);
    }

    @DeleteMapping("/{id}/pin")
    public CompilationDto unpin(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос PATCH для открепления подборки с id: {}", id);
        return compilationsService.unpin(id);
    }
}
