package org.example.eoullimback.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserIdOrderByCreatedAtDesc(long userId);
    Optional<Notification> findByQrCode(String qrCode);

    @Query("""
        SELECT n
        FROM Notification n
        JOIN FETCH n.user u
        WHERE u.id = :userId
        AND n.id = :id
""")
    Optional<Notification> findByIdWithUser(@Param("userId") Long userId, @Param("id") Long id);
}
