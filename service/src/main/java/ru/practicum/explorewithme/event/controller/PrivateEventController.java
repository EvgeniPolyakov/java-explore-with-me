package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.EventFullDto;
import ru.practicum.explorewithme.event.model.EventShortDto;
import ru.practicum.explorewithme.event.model.NewEventDto;
import ru.practicum.explorewithme.event.model.UpdateEventDto;
import ru.practicum.explorewithme.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{id}/events")
public class PrivateEventController {
    public static final String USER_ID_PATH_VARIABLE_KEY = "id";
    public static final String EVENT_ID_PATH_VARIABLE_KEY = "eventId";

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        log.info("Получен запрос GET по пути /users/{}/events", id);
        return eventService.getUserEvents(id, from, size);
    }

    @PatchMapping
    public EventFullDto updateUserEvent(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                        @Valid @RequestBody UpdateEventDto eventDto) {
        log.info("Получен запрос PATCH по пути /users/{}/events", id);
        return eventService.updateUserEvent(id, eventDto);
    }

    @PostMapping
    public EventFullDto createEventOfUser(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                          @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Получен запрос POST по пути /users/{}/events", id);
        return eventService.add(newEventDto, id);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                     @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Получен запрос GET по пути /users/{}/events/{}", id, eventId);
        return eventService.getUserEvent(id, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelUserEvent(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                        @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Получен запрос PATCH по пути /users/{}/events/{}", id, eventId);
        return eventService.cancelUserEvent(id, eventId);
    }
}
