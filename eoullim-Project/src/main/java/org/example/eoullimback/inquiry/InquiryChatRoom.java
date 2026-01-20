package org.example.eoullimback.inquiry;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "inquiry_chat_room")
public class InquiryChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;  // 사용자 ID

    @Column(name = "admin_id", nullable = false)
    private String adminId; // 관리자 ID

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public InquiryChatRoom(Long id, String userId, String adminId) {
        this.id = id;
        this.userId = userId;
        this.adminId = adminId;
    }
}
