package org.example.eoullimback.qaa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
