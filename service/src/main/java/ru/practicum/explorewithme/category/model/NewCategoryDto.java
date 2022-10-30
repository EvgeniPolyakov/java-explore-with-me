package ru.practicum.explorewithme.category.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewCategoryDto {
    @NotBlank
    @Size(max = 255)
    private String name;
}
