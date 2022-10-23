package ru.practicum.explorewithme.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserShortDto {
    private Long id;
    private String name;
}
