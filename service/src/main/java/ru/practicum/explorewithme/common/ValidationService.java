package ru.practicum.explorewithme.common;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.model.UpdateEventDto;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ValidationService {
    private static final String NO_ACCESS_MESSAGE = "User with id %s has no rights to access event with id %s.";
    public static final String UNPUBLISHED_EVENT_PARTICIPATION_ERROR_MESSAGE =
            "Error: user is trying to participate in an event that is not in a published state";
    public static final String DUPLICATE_ENTRY_MASSAGE = "Error: user has already sent a request for this event";
    public static final String OWN_EVENT_PARTICIPATION_ERROR_MESSAGE =
            "Error: user is requesting to participate in his own event";
    public static final String PARTICIPATION_LIMIT_REACHED_MESSAGE = "Participant limit has been reached";
    public static final String EVENT_NOT_IN_PENDING_STATE_MESSAGE =
            "Error: event must be in a pending state";
    public static final String EVENT_IN_PUBLISHED_STATE_MESSAGE =
            "Error: one can not reject event in a published state";
    public static final String EVENT_FOR_UPDATE_ALREADY_PUBLISHED_MESSAGE =
            "Error: one can not amend an event in a published state";
    public static final String LATE_DEADLINE_MESSAGE = "Event starts in less than %s hours";

    public void validateNewRequest(Event event, Long userId, Request request, List<Request> confirmedRequests) {
        if (request != null) {
            throw new BadRequestException(DUPLICATE_ENTRY_MASSAGE);
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException(OWN_EVENT_PARTICIPATION_ERROR_MESSAGE);
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new BadRequestException(UNPUBLISHED_EVENT_PARTICIPATION_ERROR_MESSAGE);
        }
        if (event.getParticipantLimit() == confirmedRequests.size()) {
            throw new BadRequestException(PARTICIPATION_LIMIT_REACHED_MESSAGE);
        }
    }

    public void validateDeadline(LocalDateTime date, int hours) {
        if (date.minusHours(hours).isBefore(LocalDateTime.now())) {
            throw new BadRequestException(String.format(LATE_DEADLINE_MESSAGE, hours));
        }
    }

    public void validateEventForUpdate(Long userId, UpdateEventDto eventDto, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException(String.format(NO_ACCESS_MESSAGE, userId, event.getId()));
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new BadRequestException(EVENT_FOR_UPDATE_ALREADY_PUBLISHED_MESSAGE);
        }
        if (event.getState().equals(State.CANCELED)) {
            event.setState(State.PENDING);
        }
        if (eventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new BadRequestException(String.format(LATE_DEADLINE_MESSAGE, 2));
        }
    }

    public void validateAccessToEvent(Long userId, Long eventId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException(String.format(NO_ACCESS_MESSAGE, userId, eventId));
        }
    }

    public void validatePendingState(Event event) {
        if (!event.getState().equals(State.PENDING)) {
            throw new BadRequestException(EVENT_NOT_IN_PENDING_STATE_MESSAGE);
        }
    }

    public void validatePublishedState(Event event) {
        if (event.getState().equals(State.PUBLISHED)) {
            throw new BadRequestException(EVENT_IN_PUBLISHED_STATE_MESSAGE);
        }
    }

}
