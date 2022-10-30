package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.StatsRepository;
import ru.practicum.explorewithme.model.Hit;
import ru.practicum.explorewithme.model.HitDto;
import ru.practicum.explorewithme.model.ViewStats;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Override
    public void addHit(HitDto dto) {
        log.info("Сохранение статистики для hit: {}", dto);
        Hit hit = StatsMapper.toHit(dto);
        repository.save(hit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uncodedUris, Boolean unique) {
        List<String> uris = new ArrayList<>();
        for (String u : uncodedUris) {
            uris.add(URLDecoder.decode(u, StandardCharsets.UTF_8));
        }
        log.info("Получение статистики по списку uri: {}", uris);
        List<Hit> hits = repository.findDistinctHitsByUriInAndTimestampBetween(uris, start, end);
        List<ViewStats> viewStats = hits.stream()
                .map(h -> StatsMapper.toViewStats(h, hits.size()))
                .collect(Collectors.toList());
        if (viewStats.isEmpty()) {
            return List.of(new ViewStats("unavailable", "unavailable", 0));
        }
        return viewStats;
    }
}
