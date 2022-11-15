package ru.practicum.explorewithme.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.CategoryDto;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category with id %s has not been found";
    private static final String CATEGORY_NOT_EMPTY_MESSAGE = "Category with id %s contains events";

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAll() {
        log.info("Getting the list of all categories");
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryDtoById(Long id) {
        log.info("Getting the dto of category with id {}", id);
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(CATEGORY_NOT_FOUND_MESSAGE, id)));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        log.info("Getting the category with id {}", id);
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(CATEGORY_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public CategoryDto add(Category category) {
        log.info("Adding the category: {}", category);
        Category addedCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(addedCategory);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        log.info("Updating the category with id {}", categoryDto.getId());
        Category categoryForUpdate = getCategoryById(categoryDto.getId());
        categoryForUpdate.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryForUpdate);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting the category with id {}", id);
        Category category = getCategoryById(id);
        List<Event> eventsByCategory = eventRepository.findAllByCategoryId(id);
        if (eventsByCategory.isEmpty()) {
            categoryRepository.delete(category);
            log.info("Category with id {} has been deleted", id);
        } else {
            throw new ForbiddenException(String.format(CATEGORY_NOT_EMPTY_MESSAGE, id));
        }
    }
}
