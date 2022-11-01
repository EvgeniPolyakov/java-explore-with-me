package ru.practicum.explorewithme.event.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.service.CategoryService;
import ru.practicum.explorewithme.client.RestClient;
import ru.practicum.explorewithme.common.QPredicates;
import ru.practicum.explorewithme.common.ValidationService;
import ru.practicum.explorewithme.compilation.model.NewCompilationDto;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.request.model.RequestDto;
import ru.practicum.explorewithme.request.model.Status;
import ru.practicum.explorewithme.request.service.RequestService;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.event.model.QEvent.event;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String EVENT_NOT_FOUND_MESSAGE = "Событие c id %s не найдено.";
    public static final int ONE_HOUR_VALUE = 1;
    public static final int TWO_HOURS_VALUE = 2;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository repository;
    private final RequestService requestService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ValidationService validationService;
    private final RestClient statsClient;

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getShortDtoById(Long id, HttpServletRequest request) {
        log.info("Получение события с id {}", id);
        statsClient.postHit(
                new Hit(request.getServerName(), request.getRequestURI(), request.getRemoteAddr(),
                        LocalDateTime.now().format(DATE_TIME_FORMATTER))
        );
        Event event = repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, id)));
        return toFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(Long id) {
        log.info("Получение события с id {}", id);
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAll(FilterParams params, String sort, int from, int size, HttpServletRequest request) {
        log.info("Получение всех событий с параметрами фильтра: {}", params);
        statsClient.postHit(
                new Hit(request.getServerName(), request.getRequestURI(), request.getRemoteAddr(),
                        LocalDateTime.now().format(DATE_TIME_FORMATTER))
        );

        Predicate predicate = QPredicates.builder()
                .add(params.getText(), txt -> event.annotation.containsIgnoreCase(txt)
                        .or(event.description.containsIgnoreCase(txt)))
                .add(params.getCategories(), event.category.id::in)
                .add(params.getRangeStart(), event.eventDate::after)
                .add(params.getRangeEnd(), event.eventDate::before)
                .add(params.getPaid(), event.paid::eq)
                .buildAnd();
        List<Event> events = repository.findAll(predicate, PageRequest.of(from, size)).getContent();
        if (Boolean.TRUE.equals(params.getOnlyAvailable())) {
            events = events.stream()
                    .filter(e -> (e.getParticipantLimit() != 0L)
                            && (e.getParticipantLimit() >
                            requestService.getRequestsByStatus(e.getId(), Status.CONFIRMED).size()))
                    .collect(Collectors.toList());
        }
        return events.stream()
                .map(this::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(Long id, int from, int size) {
        log.info("Получение всех событий пользователя с id {}", id);
        List<Event> events = repository.findEventsByInitiatorId(id, PageRequest.of(from, size));
        return events.stream()
                .map(this::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsUserCreatedOrJoined(Long id, int from, int size) {
        log.info("Получение всех событий, добавленных пользователем с id {}", id);
        List<Event> eventsCreated = repository.findEventsByInitiatorId(id, PageRequest.of(from, size));
        log.info("Получение всех событий с одобренными заявками от пользователя с id {}", id);
        List<Event> eventsJoined = requestService.getAllUserEventsWithConfirmedParticipation(id, PageRequest.of(from, size));
        eventsCreated.addAll(eventsJoined);
        return eventsCreated.stream()
                .map(this::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllUserFriendsEvents(List<Long> friendIds, int from, int size) {
        log.info("Получение событий, созданных друзьями или с их участием");
        return friendIds.stream()
                .map(id -> getEventsUserCreatedOrJoined(id, from, size))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto add(NewEventDto eventDto, Long id) {
        log.info("Добавление события: {}", eventDto);
        validationService.validateDeadline(eventDto.getEventDate(), TWO_HOURS_VALUE);
        Category category = categoryService.getCategoryById(eventDto.getCategory());
        User initiator = userService.getById(id);
        Event newEvent = EventMapper.toEvent(eventDto, category, initiator);
        return toFullDto(repository.save(newEvent));
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, UpdateEventDto eventDto) {
        log.info("Обновление события: {}", eventDto);
        Event event = getEventById(eventDto.getEventId());
        validationService.validateEventForUpdate(userId, eventDto, event);
        return updateEventFields(eventDto, event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        log.info("Получение события с id: {}", eventId);
        Event event = getEventById(eventId);
        validationService.validateAccessToEvent(userId, eventId, event);
        return toFullDto(event);
    }

    @Override
    public EventFullDto cancelUserEvent(Long id, Long eventId) {
        log.info("Отмена события с id: {}", eventId);
        Event event = getEventById(eventId);
        validationService.validatePendingState(event);
        event.setState(State.CANCELED);
        return toFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getEventRequests(Long userId, Long eventId) {
        log.info("Запрос заявок на событие с id: {}", eventId);
        Event event = getEventById(eventId);
        validationService.validateAccessToEvent(userId, eventId, event);
        return requestService.getAllRequestsByEvent(eventId);
    }

    @Override
    public RequestDto confirmRequest(Long userId, Long eventId, Long requestId) {
        log.info("Утверждение заявки с id: {} на событие с id: {}", requestId, eventId);
        Event event = getEventById(eventId);
        validationService.validateAccessToEvent(userId, eventId, event);
        return requestService.confirmRequest(event, userId, requestId);
    }

    @Override
    public RequestDto rejectRequest(Long userId, Long eventId, Long requestId) {
        log.info("Отклонение заявки с id: {} на событие с id: {}", requestId, eventId);
        Event event = getEventById(eventId);
        validationService.validateAccessToEvent(userId, eventId, event);
        return requestService.rejectRequest(requestId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAllByFilter(List<Long> users, List<State> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        log.info("Получение всех событий с параметрами фильтра: users {}, states {}, categories {}, start {}, end {},",
                users, states, categories, rangeStart, rangeEnd);
        Predicate predicate = QPredicates.builder()
                .add(users, event.initiator.id::in)
                .add(states, event.state::in)
                .add(categories, event.category.id::in)
                .add(rangeStart, event.eventDate::after)
                .add(rangeEnd, event.eventDate::before)
                .buildAnd();
        List<Event> events = repository.findAll(predicate, PageRequest.of(from, size)).getContent();
        return events.stream()
                .map(this::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateByAdmin(Long id, UpdateEventDto eventDto) {
        log.info("Обновление события: {}", eventDto);
        Event event = getEventById(id);
        return updateEventFields(eventDto, event);
    }

    @Override
    public EventFullDto publishByAdmin(Long id) {
        log.info("Публикация/утверждение события с id: {}", id);
        Event event = getEventById(id);
        validationService.validateDeadline(event.getEventDate(), ONE_HOUR_VALUE);
        validationService.validatePendingState(event);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(State.PUBLISHED);
        return toFullDto(event);
    }

    @Override
    public EventFullDto reject(Long id) {
        log.info("Отклонение события с id: {}", id);
        Event event = getEventById(id);
        validationService.validatePublishedState(event);
        event.setState(State.CANCELED);
        return toFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getCompilationEvents(NewCompilationDto compilationDto) {
        log.info("Получение событий по id: {}", compilationDto.getEvents());
        return new ArrayList<>(repository.findEventsByIdIn(compilationDto.getEvents()));
    }

    private EventFullDto updateEventFields(UpdateEventDto eventDto, Event event) {
        log.info("Обновление полей у события: {}", event.getId());
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getCategory() != null) {
            Category category = categoryService.getCategoryById(eventDto.getCategory());
            event.setCategory(category);
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit().intValue());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        return toFullDto(repository.save(event));
    }

    private EventShortDto toShortDto(Event event) {
        log.info("Конвертация события event: {} в формат EventShortDto", event);
        int confirmedRequests = requestService.getRequestsByStatus(event.getId(), Status.CONFIRMED).size();
        ViewStats viewStats = statsClient.getStats(
                event.getId().intValue(),
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().plusMonths(1));
        return EventMapper.toEventShortDto(event, confirmedRequests, viewStats.getHits().intValue());
    }

    private EventFullDto toFullDto(Event event) {
        log.info("Конвертация события event: {} в формат EventFullDto", event);
        int confirmedRequests = requestService.getRequestsByStatus(event.getId(), Status.CONFIRMED).size();
        ViewStats viewStats = statsClient.getStats(
                event.getId().intValue(),
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().plusMonths(1));
        return EventMapper.toEventFullDto(event, confirmedRequests, viewStats.getHits().intValue());
    }
}
