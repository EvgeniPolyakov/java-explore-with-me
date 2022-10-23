package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;

public class EventMapper {

    public static Event toEvent(NewEventDto eventDto, Category category, User initiator) {
        return new Event(
                null,
                eventDto.getTitle(),
                eventDto.getAnnotation(),
                eventDto.getDescription(),
                category,
                State.PENDING,
                LocalDateTime.now(),
                null,
                eventDto.getEventDate(),
                initiator,
                eventDto.getLocation(),
                eventDto.getParticipantLimit(),
                eventDto.getPaid(),
                eventDto.getRequestModeration()
        );
    }

    public static EventShortDto toEventShortDto(Event event, Long confirmedRequests, Long views) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory(),
                event.getEventDate(),
                event.getInitiator(),
                event.getPaid(),
                confirmedRequests,
                views
        );
    }

    public static EventFullDto toEventFullDto(Event event, Long confirmedRequests, Long views) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getDescription(),
                event.getCategory(),
                event.getState(),
                event.getCreatedOn(),
                event.getPublishedOn(),
                event.getEventDate(),
                event.getInitiator(),
                event.getLocation(),
                event.getParticipantLimit(),
                event.getPaid(),
                event.getRequestModeration(),
                views,
                confirmedRequests
        );
    }
}
