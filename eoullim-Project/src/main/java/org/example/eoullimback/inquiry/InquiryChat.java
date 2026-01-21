package org.example.eoullimback.inquiry;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.enums.inquiry.SenderType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "inquiry_chat",
    indexes = {
        @Index(name = "idx_inquiry_room_id", columnList = "inquiry_room_id"),
        @Index(name = "idx_created_at", columnList = "created_at")
    }
)
public class InquiryChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "inquiry_room_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_inquiry_chat_room")
    )
    private InquiryChatRoom room;

    @Column(nullable = false)
    private String sender;

    private String receiver;

    @Column(nullable = false)
    private String message;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SenderType senderType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public InquiryChat(InquiryChatRoom room, String sender, String receiver, String message, SenderType senderType) {
        this.room = room;
        this.sender = sender;
        this.receiver = receiver;
        this.senderType = senderType;
        this.message = message;
    }
}
