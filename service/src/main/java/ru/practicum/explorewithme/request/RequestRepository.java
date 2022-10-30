package ru.practicum.explorewithme.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.model.Status;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> getAllByRequesterId(Long id);

    List<Request> findByEventIdAndStatus(Long id, Status status);

    List<Request> getAllByEventId(Long id);

    Request getByRequesterIdAndEventId(Long userId, Long eventId);
}
