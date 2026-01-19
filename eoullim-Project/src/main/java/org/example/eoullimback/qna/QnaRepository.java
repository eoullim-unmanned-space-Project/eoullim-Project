package org.example.eoullimback.qna;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    @Query(value = "SELECT q FROM Qna q JOIN FETCH q.user ORDER BY q.createdAt DESC",
            countQuery = "SELECT COUNT(DISTINCT q) FROM Qna q")
    Page<Qna> findAllWithUserOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT DISTINCT q FROM Qna q JOIN FETCH q.user " +
            "WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(q.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY q.createdAt DESC",
            countQuery = "SELECT COUNT(DISTINCT q) FROM Qna q " +
                    "WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "   OR LOWER(q.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Qna> findByTitleContainingOrContentContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT q FROM Qna q JOIN FETCH q.user WHERE q.id = :id")
    Optional<Qna> findByIdWithUser(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Qna q SET q.viewCount = q.viewCount + 1 WHERE q.id = :id")
    void increaseViewCount(@Param("id") Long id);

    @Query(
            value = "SELECT q FROM Qna q JOIN FETCH q.user " +
                    "WHERE q.user.id = :userId " +
                    "ORDER BY q.createdAt DESC",
            countQuery = "SELECT COUNT(q) FROM Qna q WHERE q.user.id = :userId"
    )
    Page<Qna> findMyQnaList(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT COUNT(q)
    FROM Qna q
    WHERE q.createdAt >= :start
      AND q.createdAt < :end
""")
    long countToday(@Param("start") java.time.LocalDateTime start,
                    @Param("end") java.time.LocalDateTime end);
}
