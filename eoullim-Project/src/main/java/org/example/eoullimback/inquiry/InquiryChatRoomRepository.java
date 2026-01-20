package org.example.eoullimback.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InquiryChatRoomRepository extends JpaRepository<InquiryChatRoom, Long> {

    Optional<InquiryChatRoom> findByUserIdAndAdminId(String userId, String adminId);
}
