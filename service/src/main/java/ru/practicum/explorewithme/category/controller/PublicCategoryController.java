package ru.practicum.explorewithme.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class PublicCategoryController {
    public static final String ID_PATH_VARIABLE_KEY = "id";

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getAll() {
        log.info("Получен запрос GET по пути /categories");
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public Category getById(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос GET по пути /categories по id {}", id);
        return categoryService.getById(id);
    }
}
