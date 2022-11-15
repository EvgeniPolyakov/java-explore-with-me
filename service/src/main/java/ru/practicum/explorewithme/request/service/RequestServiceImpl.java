package ru.practicum.explorewithme.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.common.ValidationService;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.request.RequestRepository;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.model.RequestDto;
import ru.practicum.explorewithme.request.model.Status;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private static final String REQUEST_NOT_FOUND_MESSAGE = "Request with id %s has not been found";
    public static final String WRONG_STATE_TO_CONFIRM_REQUEST_MESSAGE = "Only pending or canceled events can be changed";
    public static final String PARTICIPATION_LIMIT_REACHED_MESSAGE = "Participant limit has been reached";

    private final UserService userService;
    private final RequestRepository repository;
    private final ValidationService validationService;

    private Request getById(Long id) {
        log.info("Getting request with id {}", id);
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(REQUEST_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public List<RequestDto> getAll(Long id) {
        log.info("Getting user {} requests for participation in other events", id);
        List<Request> requests = repository.getAllByRequesterId(id);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto add(Long userId, Event event) {
        User user = userService.getById(userId);
        log.info("Saving user {} request to participate in event {}", userId, event.getId());
        Request request = repository.getByRequesterIdAndEventId(userId, event.getId());
        validationService.validateNewRequest(event, userId, request, getRequestsByStatus(event.getId(), Status.CONFIRMED));
        Request newRequest = new Request(null, user, event, LocalDateTime.now(), Status.PENDING);
        if (!event.isRequestModeration()) {
            newRequest.setStatus(Status.CONFIRMED);
        }
        return RequestMapper.toRequestDto(repository.save(newRequest));
    }

    @Override
    @Transactional
    public RequestDto update(Long userId, Long requestId) {
        log.info("Caneling user {} request to participate in event {}", userId, requestId);
        Request request = getById(requestId);
        request.setStatus(Status.CANCELED);
        return RequestMapper.toRequestDto(repository.save(request));
    }

    @Override
    public List<Request> getRequestsByStatus(Long id, Status status) {
        log.info("Getting requests in {} status for event {}", status, id);
        return repository.findByEventIdAndStatus(id, status);
    }

    @Override
    public List<RequestDto> getAllRequestsByEvent(Long eventId) {
        log.info("Getting all requests for event {}", eventId);
        List<Request> requests = repository.getAllByEventId(eventId);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getAllUserEventsWithConfirmedParticipation(Long id, PageRequest pageRequest) {
        log.info("Getting all confirmed requests from user {}", id);
        List<Request> requests = repository.findByRequesterIdAndStatus(id, Status.CONFIRMED, pageRequest);
        return requests.stream()
                .map(Request::getEvent)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getAllUserEventsWithConfirmedParticipation(List<Long> ids, PageRequest pageRequest) {
        log.info("Getting all events with confirmed request status for users with provided ids");
        List<Request> requests = repository.findByRequesterIdInAndStatus(ids, Status.CONFIRMED, pageRequest);
        return requests.stream()
                .map(Request::getEvent)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto rejectRequest(Long requestId) {
        log.info("Rejecting request {}", requestId);
        Request request = getById(requestId);
        request.setStatus(Status.REJECTED);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    @Transactional
    public RequestDto confirmRequest(Event event, Long userId, Long requestId) {
        log.info("Confirming request {}", requestId);
        Request request = getById(requestId);
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            request.setStatus(Status.CONFIRMED);
            add(userId, event);
            throw new BadRequestException(WRONG_STATE_TO_CONFIRM_REQUEST_MESSAGE);
        }
        if (event.getParticipantLimit() == getRequestsByStatus(event.getId(), Status.CONFIRMED).size()) {
            List<Request> pendingRequests = getRequestsByStatus(event.getId(), Status.PENDING);
            for (Request r : pendingRequests) {
                r.setStatus(Status.REJECTED);
                add(userId, event);
            }
            throw new BadRequestException(PARTICIPATION_LIMIT_REACHED_MESSAGE);
        }
        request.setStatus(Status.CONFIRMED);
        return RequestMapper.toRequestDto(repository.save(request));
    }
}
