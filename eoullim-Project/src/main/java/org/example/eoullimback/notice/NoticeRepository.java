package org.example.eoullimback.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query(value = "SELECT n FROM Notice n JOIN FETCH n.user ORDER BY n.createdAt DESC",
            countQuery = "SELECT COUNT(DISTINCT n) FROM Notice n")
    Page<Notice> findAllWithUserOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT DISTINCT n FROM Notice n JOIN FETCH n.user " +
            "WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY n.createdAt DESC",
            countQuery = "SELECT COUNT(DISTINCT n) FROM Notice n " +
                    "WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "   OR LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Notice> findByTitleContainingOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
}
