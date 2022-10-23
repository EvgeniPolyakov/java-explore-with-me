package ru.practicum.explorewithme.category.service;

import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.NewCategoryDto;

public class CategoryMapper {

    public static Category toCategory(NewCategoryDto categoryDto) {
        return new Category(
                null,
                categoryDto.getName()
        );
    }
}
