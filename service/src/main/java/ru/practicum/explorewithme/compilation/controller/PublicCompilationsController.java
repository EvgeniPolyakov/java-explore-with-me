package ru.practicum.explorewithme.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.service.CompilationsService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicCompilationsController {
    public static final String ID_PATH_VARIABLE_KEY = "id";

    private final CompilationsService compilationsService;

    @GetMapping
    public List<Compilation> getAll(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("Получен запрос GET по пути /compilations");
        return compilationsService.getAll(pinned, from, size);
    }

    @GetMapping("/{id}")
    public Compilation getById(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос GET по пути /compilations по id {}", id);
        return compilationsService.getById(id);
    }

}
