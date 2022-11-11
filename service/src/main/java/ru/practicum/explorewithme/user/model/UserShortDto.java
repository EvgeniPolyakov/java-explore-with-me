package ru.practicum.explorewithme.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserShortDto {
    private Long id;
    private String name;
}
