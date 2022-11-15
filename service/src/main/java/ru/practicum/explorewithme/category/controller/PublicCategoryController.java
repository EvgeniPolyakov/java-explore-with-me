package ru.practicum.explorewithme.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.category.model.CategoryDto;
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
    public List<CategoryDto> getAll() {
        log.info("Received GET request on /categories");
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Received GET request on /categories with id {}", id);
        return categoryService.getCategoryDtoById(id);
    }
}
