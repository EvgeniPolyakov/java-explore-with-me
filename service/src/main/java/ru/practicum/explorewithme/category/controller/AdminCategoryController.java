package ru.practicum.explorewithme.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.NewCategoryDto;
import ru.practicum.explorewithme.category.service.CategoryMapper;
import ru.practicum.explorewithme.category.service.CategoryService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    public static final String ID_PATH_VARIABLE_KEY = "id";

    private final CategoryService categoryService;

    @PostMapping
    public Category add(@RequestBody NewCategoryDto categoryDto) {
        log.info("Получен запрос POST по пути /admin/categories: {}", categoryDto);
        Category category = CategoryMapper.toCategory(categoryDto);
        return categoryService.add(category);
    }

    @PatchMapping
    public Category update(@RequestBody Category category) {
        log.info("Получен запрос PATCH по пути /admin/categories: {}", category);
        return categoryService.update(category);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос DELETE по пути /admin/categories/ по id {}", id);
        categoryService.delete(id);
    }
}
