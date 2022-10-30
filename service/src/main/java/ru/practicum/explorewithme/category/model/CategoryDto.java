package ru.practicum.explorewithme.category.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank
    @Size(max = 255)
    private String name;
}
