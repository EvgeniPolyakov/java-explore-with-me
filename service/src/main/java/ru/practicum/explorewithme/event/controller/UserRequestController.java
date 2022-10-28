package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.request.model.RequestDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{id}/events/{eventId}/requests")
public class UserRequestController {
    public static final String USER_ID_PATH_VARIABLE_KEY = "id";
    public static final String EVENT_ID_PATH_VARIABLE_KEY = "eventId";
    public static final String REQ_ID_PATH_VARIABLE_KEY = "reqId";

    private final EventService eventService;

    @GetMapping
    public List<RequestDto> getEventRequests(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                             @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Получен запрос GET по пути /users/{}/events/{}/requests", id, eventId);
        return eventService.getEventRequests(id, eventId);
    }

    @PatchMapping("/{reqId}/confirm")
    public RequestDto confirmRequest(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                     @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId,
                                     @PathVariable(REQ_ID_PATH_VARIABLE_KEY) Long requestId) {
        log.info("Получен запрос PATCH по пути /users/{}/events/{}/requests/{}/confirm", id, eventId, requestId);
        return eventService.confirmRequest(id, eventId, requestId);
    }

    @PatchMapping("/{reqId}/reject")
    public RequestDto rejectRequest(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                    @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId,
                                    @PathVariable(REQ_ID_PATH_VARIABLE_KEY) Long requestId) {
        log.info("Получен запрос PATCH по пути /users/{}/events/{}/requests/{}/reject", id, eventId, requestId);
        return eventService.rejectRequest(id, eventId, requestId);
    }
}
