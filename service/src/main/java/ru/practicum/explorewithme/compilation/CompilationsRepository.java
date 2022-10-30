package ru.practicum.explorewithme.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.compilation.model.Compilation;

public interface CompilationsRepository extends JpaRepository<Compilation, Long> {
}
