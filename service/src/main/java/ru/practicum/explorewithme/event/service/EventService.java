package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.compilation.model.NewCompilationDto;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.request.model.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    EventFullDto getShortDtoById(Long id, HttpServletRequest request);

    List<Event> getCompilationEvents(NewCompilationDto compilationDto);

    Event getEventById(Long id);

    List<EventShortDto> getAll(FilterParams params, String sort, int from, int size, HttpServletRequest request);

    List<EventShortDto> getUserEvents(Long id, int from, int size);

    Set<EventShortDto> getAllUserFriendsEvents(List<Long> friendIds, int from, int size);

    List<EventShortDto> getEventsUserCreatedOrJoined(Long id, int from, int size);

    EventFullDto add(NewEventDto eventDto, Long id);

    EventFullDto updateUserEvent(Long id, UpdateEventDto eventDto);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto cancelUserEvent(Long id, Long eventId);

    RequestDto rejectRequest(Long id, Long eventId, Long requestId);

    RequestDto confirmRequest(Long id, Long eventId, Long requestId);

    List<RequestDto> getEventRequests(Long id, Long eventId);

    List<EventFullDto> getAllByFilter(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateByAdmin(Long id, UpdateEventDto eventDto);

    EventFullDto publishByAdmin(Long id);

    EventFullDto reject(Long id);
}
