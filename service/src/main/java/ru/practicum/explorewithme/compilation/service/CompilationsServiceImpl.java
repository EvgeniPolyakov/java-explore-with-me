package ru.practicum.explorewithme.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.compilation.CompilationsRepository;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.model.CompilationDto;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {
    private static final String COMPILATION_NOT_FOUND_MESSAGE = "Compilation with id %s has not been found";

    private final CompilationsRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        log.info("Getting the list of all compilations");
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> compilations = repository.findAll(pageable).getContent();
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationDtoById(Long id) {
        log.info("Getting compilation dto with id {}", id);
        Compilation compilation = repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(COMPILATION_NOT_FOUND_MESSAGE, id)));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional(readOnly = true)
    public Compilation getCompilationById(Long id) {
        log.info("Getting compilation with id {}", id);
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(COMPILATION_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public CompilationDto add(Compilation compilation) {
        log.info("Adding the compilation: {}", compilation);
        Compilation newCompilation = repository.save(compilation);
        return CompilationMapper.toCompilationDto(newCompilation);
    }

    @Override
    public CompilationDto update(Long compilationId, Long eventId, Event event) {
        log.info("Adding the event with id {} to compilation with id {}", eventId, compilationId);
        Compilation compilation = getCompilationById(compilationId);
        compilation.getEvents().add(event);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting the compilation with id {}", id);
        Compilation compilation = getCompilationById(id);
        repository.delete(compilation);
        log.info("Compilation with id {} has been deleted", id);
    }

    @Override
    public CompilationDto pin(Long id) {
        log.info("Pinning compilation with id {} to the page", id);
        Compilation compilation = getCompilationById(id);
        compilation.setPinned(true);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDto unpin(Long id) {
        log.info("Unpinning compilation with id {} from the page", id);
        Compilation compilation = getCompilationById(id);
        compilation.setPinned(false);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public void deleteEvent(Long compilationId, Long eventId, Event event) {
        log.info("Deleting event with id {} from compilation with id {}", eventId, compilationId);
        Compilation compilation = getCompilationById(compilationId);
        compilation.getEvents().remove(event);
        log.info("Event with id {} has been deleted from compilation with id {}", eventId, compilationId);
    }
}
