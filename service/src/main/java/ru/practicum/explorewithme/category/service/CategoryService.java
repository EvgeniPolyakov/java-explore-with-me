package ru.practicum.explorewithme.category.service;

import ru.practicum.explorewithme.category.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();

    Category getById(Long id);

    Category add(Category category);

    Category update(Category category);

    void delete(Long id);
}
