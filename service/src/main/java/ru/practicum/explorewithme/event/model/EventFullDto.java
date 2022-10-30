package ru.practicum.explorewithme.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto extends EventShortDto {
    private String description;
    private State state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Location location;
    private int participantLimit;
    private boolean requestModeration;

    public EventFullDto(Long id, String title, String annotation, Category category, LocalDateTime eventDate,
                        User initiator, Boolean paid, int confirmedRequests, int views, String description,
                        State state, LocalDateTime createdOn, LocalDateTime publishedOn, Location location,
                        int participantLimit, boolean requestModeration) {
        super(id, title, annotation, category, eventDate, initiator, paid, confirmedRequests, views);
        this.description = description;
        this.state = state;
        this.createdOn = createdOn;
        this.publishedOn = publishedOn;
        this.location = location;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
    }
}
