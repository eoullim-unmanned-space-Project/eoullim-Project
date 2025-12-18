package org.example.eoullimback.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.qaa " +
            "JOIN FETCH c.user " +
            "WHERE c.qaa.id = :qaaId " +
            "ORDER BY c.createdAt ASC ")
    List<Comment> findByQaaIdWithUser(@Param("qaaId") Long qaaId);

    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.user " +
            "JOIN FETCH c.qaa " +
            "WHERE c.id = :id ")
    Optional<Comment> findByWithUser(@Param("id") Long id);

    void deleteByQaaId(Long qaaId);
}
