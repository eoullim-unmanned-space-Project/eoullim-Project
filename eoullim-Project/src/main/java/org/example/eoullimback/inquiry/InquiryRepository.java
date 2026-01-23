package org.example.eoullimback.inquiry;

import org.example.eoullimback._common.enums.inquiry.SenderType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends CrudRepository<InquiryChat, Long> {

    @Query("SELECT ic FROM InquiryChat ic WHERE ic.room.id = :roomId ORDER BY ic.createdAt DESC LIMIT 1")
    Optional<InquiryChat> findLatestChatByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT ic FROM InquiryChat ic WHERE ic.room.id = :roomId ORDER BY ic.createdAt ASC")
    List<InquiryChat> findAllByRoomIdOrderByCreatedAtAsc(@Param("roomId") Long roomId);

    void saveAndFlush(InquiryChat inquiryChat);
}
