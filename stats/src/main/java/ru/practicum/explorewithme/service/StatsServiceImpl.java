package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.StatsRepository;
import ru.practicum.explorewithme.model.Hit;
import ru.practicum.explorewithme.model.ViewStats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Override
    public void addHit(Hit hit) {
        log.info("Сохранение статистики для hit: {}", hit);
        repository.save(hit);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Получение статистики по списку uri: {}", uris);
        List<ViewStats> stats = new ArrayList<>();
        for (String uri : uris) {
            ViewStats stat = StatsMapper.toViewStats(repository.findDistinctHitsByUriAndTimestampBetween(uri, start, end));
            stats.add(stat);
        }
        return stats;
    }
}
