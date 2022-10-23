package ru.practicum.explorewithme.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.model.UpdateEventDto;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {
    private static final String NO_ACCESS_MESSAGE = "Пользователь с id %s не имеет доступа к событию с id %s.";
    public static final String UNPUBLISHED_EVENT_PARTICIPATION_ERROR_MESSAGE =
            "Ошибка: попытка принять участие в неопубликованном событии";
    public static final String DUPLICATE_ENTRY_MASSAGE = "Ошибка: попытка добавления повторного запроса";
    public static final String OWN_EVENT_PARTICIPATION_ERROR_MESSAGE =
            "Ошибка: попытка пользователя добавить запрос на участие в своём же событии";
    public static final String PARTICIPATION_LIMIT_REACHED_MESSAGE = "Достигнут лимит по заявкам на данное событие";
    public static final String EVENT_NOT_IN_PENDING_STATE_MESSAGE =
            "Ошибка: событие должно быть в состоянии ожидания публикации";
    public static final String EVENT_IN_PUBLISHED_STATE_MESSAGE =
            "Ошибка: нельзя отменить событие в опубликованном состоянии";
    public static final String EVENT_FOR_UPDATE_ALREADY_PUBLISHED_MESSAGE =
            "Ошибка: попытка внесение изменений в опубликованное событие";
    public static final String LATE_DEADLINE_MESSAGE = "До события осталось менее %s часа/часов";

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
