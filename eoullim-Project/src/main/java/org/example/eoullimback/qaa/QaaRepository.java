package org.example.eoullimback.qaa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QaaRepository extends JpaRepository<Qaa, Long> {

    @Query(value = "SELECT q FROM Qaa q JOIN FETCH q.user ORDER BY q.createdAt DESC",
            countQuery = "SELECT COUNT(DISTINCT q) FROM Qaa q")
    Page<Qaa> findAllWithUserOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT DISTINCT q FROM Qaa q JOIN FETCH q.user " +
            "WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(q.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY q.createdAt DESC",
            countQuery = "SELECT COUNT(DISTINCT q) FROM Qaa q " +
                    "WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "   OR LOWER(q.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Qaa> findByTitleContainingOrContentContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT q FROM Qaa q JOIN FETCH q.user WHERE q.id = :id")
    Optional<Qaa> findByIdWithUser(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Qaa q SET q.viewCount = q.viewCount + 1 WHERE q.id = :id")
    void increaseViewCount(@Param("id") Long id);

    @Query(
            value = "SELECT q FROM Qaa q JOIN FETCH q.user " +
                    "WHERE q.user.id = :userId " +
                    "ORDER BY q.createdAt DESC",
            countQuery = "SELECT COUNT(q) FROM Qaa q WHERE q.user.id = :userId"
    )
    Page<Qaa> findMyQaaList(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT COUNT(q)
    FROM Qaa q
    WHERE q.createdAt >= :start
      AND q.createdAt < :end
""")
    long countToday(@Param("start") java.time.LocalDateTime start,
                    @Param("end") java.time.LocalDateTime end);
}
