package ru.practicum.explorewithme.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.request.model.RequestDto;
import ru.practicum.explorewithme.request.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{id}/requests")
public class RequestController {
    public static final String ID_PATH_VARIABLE_KEY = "id";

    private final RequestService requestService;
    private final EventService eventService;

    @GetMapping
    public List<RequestDto> getAll(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Received GET request on /users/{}/requests", id);
        return requestService.getAll(id);
    }

    @PostMapping
    public RequestDto add(@PathVariable(ID_PATH_VARIABLE_KEY) Long userId, @RequestParam Long eventId) {
        log.info("Received POST request on /users/{}/requests", userId);
        Event event = eventService.getEventById(eventId);
        return requestService.add(userId, event);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto update(@PathVariable(ID_PATH_VARIABLE_KEY) Long userId, @PathVariable Long requestId) {
        log.info("Received PATCH request on /users/{}/requests", userId);
        return requestService.update(userId, requestId);
    }
}
