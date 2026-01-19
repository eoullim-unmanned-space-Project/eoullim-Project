package org.example.eoullimback.event;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "events",
        uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_date", columnNames = {"user_id", "fortune_date"})
        },
        indexes = {
                @Index(name = "idx_user_created", columnList = "user_id, created_at")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_event"))
    private User user;

    @Column(nullable = false)
    private int luckyScore;

    @Column(nullable = false)
    private String luckyItem;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDate fortuneDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Event(Long id,  User user, int luckyScore, String luckyItem, String content) {
        this.id = id;
        this.user = user;
        this.luckyScore = luckyScore;
        this.luckyItem = luckyItem;
        this.content = content;
        this.fortuneDate = LocalDate.now();
    }
}
