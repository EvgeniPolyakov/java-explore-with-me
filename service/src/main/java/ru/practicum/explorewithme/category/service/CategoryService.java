package ru.practicum.explorewithme.category.service;

import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAll();

    CategoryDto getCategoryDtoById(Long id);

    Category getCategoryById(Long id);

    CategoryDto add(Category category);

    CategoryDto update(CategoryDto category);

    void delete(Long id);
}
