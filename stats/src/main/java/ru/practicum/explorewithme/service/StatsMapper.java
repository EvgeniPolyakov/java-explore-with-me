package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.model.Hit;
import ru.practicum.explorewithme.model.ViewStats;

import java.util.List;

public class StatsMapper {
    public static ViewStats toViewStats(List<Hit> hits) {
        if (hits.isEmpty()) {
            return new ViewStats("unavailable", "unavailable", 0);
        }
        return new ViewStats(
                hits.get(0).getApp(),
                hits.get(0).getUri(),
                hits.size()
        );
    }
}
