package org.example.eoullimback.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InquiryChatRoomRepository extends JpaRepository<InquiryChatRoom, Long> {
    Optional<InquiryChatRoom> findByUserId(Long id);

    @Query("SELECT DISTINCT r FROM InquiryChatRoom r JOIN FETCH r.user")
    List<InquiryChatRoom> findAllWithUser();

    @Query("SELECT r FROM InquiryChatRoom r JOIN FETCH r.user WHERE r.id = :roomId")
    Optional<InquiryChatRoom> findByIdWithUser(@Param("roomId") Long roomId);
}
