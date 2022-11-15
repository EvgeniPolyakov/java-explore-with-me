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
        log.info("Received POST request on /admin/compilations: {}", compilationDto);
        List<Event> events = eventService.getCompilationEvents(compilationDto);
        Compilation compilation = CompilationMapper.toCompilation(compilationDto, events);
        return compilationsService.add(compilation);
    }

    @PatchMapping("/{id}/events/{eventId}")
    public CompilationDto addEvent(@PathVariable(ID_PATH_VARIABLE_KEY) Long compilationId,
                                   @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Received PATCH request on /admin/compilations (id: {}, eventId: {})", compilationId, eventId);
        Event eventToAdd = eventService.getEventById(eventId);
        return compilationsService.update(compilationId, eventId, eventToAdd);
    }

    @PatchMapping("/{id}/pin")
    public CompilationDto pin(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Received PATCH request to pin compilation with id {}", id);
        return compilationsService.pin(id);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Received GET request on /admin/compilations/ with id {}", id);
        compilationsService.delete(id);
    }

    @DeleteMapping("/{id}/events/{eventId}")
    public void removeEvent(@PathVariable(ID_PATH_VARIABLE_KEY) Long compilationId,
                            @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Received DELETE request on /admin/compilations (id: {}, eventId: {})", compilationId, eventId);
        Event eventToRemove = eventService.getEventById(eventId);
        compilationsService.deleteEvent(compilationId, eventId, eventToRemove);
    }

    @DeleteMapping("/{id}/pin")
    public CompilationDto unpin(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Received PATCH request to unpin compilation with id {}", id);
        return compilationsService.unpin(id);
    }
}
