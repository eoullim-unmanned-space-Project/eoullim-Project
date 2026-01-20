package org.example.eoullimback.inquiry;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "inquiry_chat")
public class InquiryChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_room_id", nullable = false)
    private InquiryChatRoom room;

    @Column(nullable = false)
    private String sender; // 작성자

    @Column(nullable = false)
    private String receiver; // 수신자

    @Column(nullable = false)
    private String message; // 메시지 내용

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public InquiryChat(Long id, InquiryChatRoom room, String sender, String receiver, String message) {
        this.id = id;
        this.room = room;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }
}
