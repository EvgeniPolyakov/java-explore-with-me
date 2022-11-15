package ru.practicum.explorewithme.event.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event toEvent(NewEventDto eventDto, Category category, User initiator) {
        return new Event(
                null,
                eventDto.getTitle(),
                eventDto.getAnnotation(),
                eventDto.getDescription(),
                category,
                State.PENDING,
                null,
                null,
                eventDto.getEventDate(),
                initiator,
                eventDto.getLocation(),
                eventDto.getParticipantLimit(),
                eventDto.isPaid(),
                eventDto.isRequestModeration()
        );
    }

    public static EventShortDto toEventShortDto(Event event, int confirmedRequests, int views) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory(),
                event.getEventDate(),
                event.getInitiator(),
                event.isPaid(),
                confirmedRequests,
                views
        );
    }

    public static EventFullDto toEventFullDto(Event event, int confirmedRequests, int views) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory(),
                event.getEventDate(),
                event.getInitiator(),
                event.isPaid(),
                confirmedRequests,
                views,
                event.getDescription(),
                event.getState(),
                event.getCreatedOn(),
                event.getPublishedOn(),
                event.getLocation(),
                event.getParticipantLimit(),
                event.isRequestModeration()
        );
    }
}
