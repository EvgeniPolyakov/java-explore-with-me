package ru.practicum.explorewithme.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.model.App;
import ru.practicum.explorewithme.model.Hit;
import ru.practicum.explorewithme.model.HitDto;
import ru.practicum.explorewithme.model.ViewStats;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatsMapper {
    public static ViewStats toViewStats(Hit hits, int totalHits) {
        return new ViewStats(
                hits.getApp().getAppName(),
                hits.getUri(),
                totalHits
        );
    }

    public static Hit toHit(HitDto dto) {
        return new Hit(
                null,
                new App(null, dto.getApp()),
                dto.getUri(),
                dto.getIp(),
                dto.getTimestamp()
        );
    }
}
