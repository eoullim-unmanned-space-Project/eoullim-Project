package org.example.eoullimback.sse;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name= "sse_chat")
@Entity
public class SseChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    @Column(nullable = false)
    private String message;

    @Builder
    public SseChat(Long id, String sender, String message) {
        this.id = id;
        this.sender = sender;
        this.message = message;
    }
}
