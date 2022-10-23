package ru.practicum.explorewithme;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    List<Hit> findDistinctHitsByUriAndTimestampBetween(String uri, LocalDateTime start, LocalDateTime end);

}
