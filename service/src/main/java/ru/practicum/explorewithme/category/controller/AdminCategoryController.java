package ru.practicum.explorewithme.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.CategoryDto;
import ru.practicum.explorewithme.category.model.NewCategoryDto;
import ru.practicum.explorewithme.category.service.CategoryMapper;
import ru.practicum.explorewithme.category.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    public static final String ID_PATH_VARIABLE_KEY = "id";

    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto add(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("Received POST request on /admin/categories: {}", categoryDto);
        Category category = CategoryMapper.toCategory(categoryDto);
        return categoryService.add(category);
    }

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Received PATCH request on /admin/categories: {}", categoryDto);
        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Received DELETE request on /admin/categories/ with id {}", id);
        categoryService.delete(id);
    }
}
