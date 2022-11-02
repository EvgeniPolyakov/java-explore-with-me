package ru.practicum.explorewithme.request.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.model.RequestDto;
import ru.practicum.explorewithme.request.model.Status;

import java.util.List;

public interface RequestService {
    List<RequestDto> getAll(Long id);

    RequestDto add(Long userId, Event event);

    RequestDto update(Long userId, Long requestId);

    List<Request> getRequestsByStatus(Long id, Status status);

    List<RequestDto> getAllRequestsByEvent(Long eventId);

    List<Event> getAllUserEventsWithConfirmedParticipation(Long id, PageRequest pageRequest);

    RequestDto rejectRequest(Long requestId);

    RequestDto confirmRequest(Event event, Long userId, Long requestId);
}
