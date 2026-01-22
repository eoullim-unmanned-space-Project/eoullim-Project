package org.example.eoullimback.inquiry;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "inquiry_chat_room",
    indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_admin_id", columnList = "admin_id")
    }
)
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_inquiry_chat_room_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "admin_id",
        foreignKey = @ForeignKey(name = "fk_inquiry_chat_room_admin")
    )
    private User admin;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public InquiryChatRoom(User user, User admin) {
        this.user = user;
        this.admin = admin;
    }

    public void setAdminId (User admin) {
        this.admin = admin;
    }

    public boolean isOwer(Long userId) {
        if (!this.user.getId().equals(userId)) {
            return false;
        }

        return true;
    }}
