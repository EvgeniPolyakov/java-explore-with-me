package ru.practicum.explorewithme.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.CompilationDto;
import ru.practicum.explorewithme.compilation.service.CompilationsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/compilations")
public class PublicCompilationsController {
    public static final String ID_PATH_VARIABLE_KEY = "id";

    private final CompilationsService compilationsService;

    @GetMapping
    public List<CompilationDto> getAll(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Positive int size
    ) {
        log.info("Received GET request on /compilations");
        return compilationsService.getAll(pinned, from, size);
    }

    @GetMapping("/{id}")
    public CompilationDto getById(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Received GET request on /compilations with id {}", id);
        return compilationsService.getCompilationDtoById(id);
    }

}
