package ru.practicum.explorewithme.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findAllByCategoryId(Long id);

    List<Event> findEventsByIdIn(List<Long> eventId);

    List<Event> findEventsByInitiatorId(Long userId, PageRequest pageRequest);
}
