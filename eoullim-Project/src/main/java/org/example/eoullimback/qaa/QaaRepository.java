package org.example.eoullimback.qaa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QaaRepository extends JpaRepository<Qaa, Long> {

    List<Qaa> findAllByOrderByIdDesc();

    @Modifying
    @Query("UPDATE Qaa q set q.viewCount = q.viewCount + 1 where q.id = :id")
    void increaseViewCount(@Param("id") Long id);
}
