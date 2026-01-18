package org.example.eoullimback.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    Optional<Event> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

}
