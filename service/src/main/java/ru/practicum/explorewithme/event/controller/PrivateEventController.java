package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.EventFullDto;
import ru.practicum.explorewithme.event.model.EventShortDto;
import ru.practicum.explorewithme.event.model.NewEventDto;
import ru.practicum.explorewithme.event.model.UpdateEventDto;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.request.model.RequestDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{id}/events")
public class PrivateEventController {
    public static final String USER_ID_PATH_VARIABLE_KEY = "id";
    public static final String EVENT_ID_PATH_VARIABLE_KEY = "eventId";
    public static final String REQ_ID_PATH_VARIABLE_KEY = "reqId";

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос GET по пути /users/{}/events", id);
        return eventService.getUserEvents(id, from, size);
    }

    @PatchMapping
    public EventFullDto updateUserEvent(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                        @RequestBody UpdateEventDto eventDto) {
        log.info("Получен запрос PATCH по пути /users/{}/events", id);
        return eventService.updateUserEvent(id, eventDto);
    }

    @PostMapping
    public EventFullDto createEventOfUser(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                          @RequestBody NewEventDto newEventDto) {
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

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getEventRequests(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                             @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Получен запрос PATCH по пути /users/{}/events/{}/requests", id, eventId);
        return eventService.getEventRequests(id, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequest(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                     @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId,
                                     @PathVariable(REQ_ID_PATH_VARIABLE_KEY) Long requestId) {
        log.info("Получен запрос PATCH по пути /users/{}/events/{}/requests/{}/confirm", id, eventId, requestId);
        return eventService.confirmRequest(id, eventId, requestId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequest(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                    @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId,
                                    @PathVariable(REQ_ID_PATH_VARIABLE_KEY) Long requestId) {
        log.info("Получен запрос PATCH по пути /users/{}/events/{}/requests/{}/reject", id, eventId, requestId);
        return eventService.rejectRequest(id, eventId, requestId);
    }
}
