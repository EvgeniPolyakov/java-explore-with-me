package ru.practicum.explorewithme.category.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.CategoryDto;
import ru.practicum.explorewithme.category.model.NewCategoryDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    public static Category toCategory(NewCategoryDto categoryDto) {
        return new Category(
                null,
                categoryDto.getName(),
                null
        );
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
