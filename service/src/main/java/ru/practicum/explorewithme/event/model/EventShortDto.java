package ru.practicum.explorewithme.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;
    private Category category;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private User initiator;
    private boolean paid;
    private int confirmedRequests;
    private int views;

    public EventShortDto(Long id, String title, String annotation, Category category, LocalDateTime eventDate,
                         User initiator, boolean paid, int confirmedRequests, int views) {
        this.id = id;
        this.title = title;
        this.annotation = annotation;
        this.category = category;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.paid = paid;
        this.confirmedRequests = confirmedRequests;
        this.views = views;
    }
}
