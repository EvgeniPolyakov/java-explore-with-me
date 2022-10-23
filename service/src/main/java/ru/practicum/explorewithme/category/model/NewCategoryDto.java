package ru.practicum.explorewithme.category.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    private Long id;
    @NotBlank
    private String name;
}
