package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.EventFullDto;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.model.UpdateEventDto;
import ru.practicum.explorewithme.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
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
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Positive int size
    ) {
        log.info("Received GET request on /admin/events");
        return eventService.getAllByFilter(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{id}")
    public EventFullDto adminUpdateEventRequest(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                                @Valid @RequestBody UpdateEventDto eventDto) {
        log.info("Received PUT request on /admin/events/{}", id);
        return eventService.updateByAdmin(id, eventDto);
    }

    @PatchMapping("/{id}/publish")
    public EventFullDto adminPublishEventRequest(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Received PATCH request on /admin/events/{}/publish", id);
        return eventService.publishByAdmin(id);
    }

    @PatchMapping("/{id}/reject")
    public EventFullDto reject(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Received PATCH request on /admin/events/{}/reject", id);
        return eventService.reject(id);
    }
}
