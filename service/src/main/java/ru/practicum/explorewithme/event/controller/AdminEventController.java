package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.EventFullDto;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.model.UpdateEventDto;
import ru.practicum.explorewithme.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    public static final String USER_ID_PATH_VARIABLE_KEY = "id";

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllByFilter(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("Получен запрос GET по пути /admin/events");
        return eventService.getAllByFilter(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{id}")
    public EventFullDto update(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id, @RequestBody UpdateEventDto eventDto) {
        log.info("Получен запрос PUT по пути /admin/events/{}", id);
        return eventService.updateEvent(id, eventDto);
    }

    @PatchMapping("/{id}/publish")
    public EventFullDto publish(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос PATCH по пути /admin/events/{}/publish", id);
        return eventService.publish(id);
    }

    @PatchMapping("/{id}/reject")
    public EventFullDto reject(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос PATCH по пути /admin/events/{}/reject", id);
        return eventService.reject(id);
    }
}
