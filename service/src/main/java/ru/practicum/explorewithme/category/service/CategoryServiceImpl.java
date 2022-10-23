package ru.practicum.explorewithme.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.exception.NotFoundException;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_NOT_FOUND_MESSAGE = "категория c id %s не найдена.";
    private static final String CATEGORY_NOT_EMPTY_MESSAGE = "категория c id %s содержит события.";

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        log.info("Получение списка всех категорий");
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getById(Long id) {
        log.info("Получение категории с id {}", id);
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(CATEGORY_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public Category add(Category category) {
        log.info("Добавление категории: {}", category);
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        log.info("Обновление категории с id {}", category.getId());
        Category categoryForUpdate = getById(category.getId());
        if (category.getName() != null) {
            categoryForUpdate.setName(category.getName());
        }
        return categoryRepository.save(categoryForUpdate);
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление категории с id {}", id);
        Category category = getById(id);
        List<Event> eventsByCategory = eventRepository.findAllByCategoryId(id);
        if (eventsByCategory.isEmpty()) {
            categoryRepository.delete(category);
        } else {
            throw new NotFoundException(String.format(CATEGORY_NOT_EMPTY_MESSAGE, id));
        }
    }
}
