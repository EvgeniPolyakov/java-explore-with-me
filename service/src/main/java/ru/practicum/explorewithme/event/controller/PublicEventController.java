package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.EventFullDto;
import ru.practicum.explorewithme.event.model.EventShortDto;
import ru.practicum.explorewithme.event.model.FilterParams;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exception.BadRequestException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/events")
public class PublicEventController {
    public static final String ID_PATH_VARIABLE_KEY = "id";

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getAllById(@RequestParam(required = false) String text,
                                          @RequestParam(required = false) Long[] categories,
                                          @RequestParam(required = false) Boolean paid,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                          @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                          @RequestParam(required = false) String sort,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(defaultValue = "10") @Positive int size,
                                          HttpServletRequest request) {
        log.info("Получен запрос GET по пути /events");
        if (!sort.equals("EVENT_DATE") && !sort.equals("VIEWS")) {
            throw new BadRequestException("Ошибка: указан некорректный тип сортировки");
        }
        FilterParams params = new FilterParams(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        return eventService.getAll(params, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable(ID_PATH_VARIABLE_KEY) Long id, HttpServletRequest request) {
        log.info("Получен запрос GET по пути /events по id {}", id);
        return eventService.getShortDtoById(id, request);
    }
}