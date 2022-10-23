package ru.practicum.explorewithme.request.service;

import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.model.RequestDto;
import ru.practicum.explorewithme.request.model.Status;

import java.util.List;

public interface RequestService {
    Request getById(Long id);

    List<RequestDto> getAll(Long id);

    RequestDto add(Long userId, Event event);

    RequestDto update(Long userId, Long requestId);

    List<Request> getRequestsByStatus(Long id, Status status);

    List<RequestDto> getAllRequestsByEvent(Long eventId);

    RequestDto rejectRequest(Long requestId);

    RequestDto confirmRequest(Event event, Long userId, Long requestId);
}
