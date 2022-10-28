package ru.practicum.explorewithme.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.model.App;
import ru.practicum.explorewithme.model.Hit;
import ru.practicum.explorewithme.model.HitDto;
import ru.practicum.explorewithme.model.ViewStats;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatsMapper {
    public static ViewStats toViewStats(List<Hit> hits) {
        if (hits.isEmpty()) {
            return new ViewStats("unavailable", "unavailable", 0);
        }
        return new ViewStats(
                hits.get(0).getApp().getAppName(),
                hits.get(0).getUri(),
                hits.size()
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
