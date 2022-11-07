package ru.practicum.explorewithme.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FilterParams {
    private String text;
    private Long[] categories;
    private boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private boolean onlyAvailable;
}
