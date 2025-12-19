package org.example.eoullimback.qaa;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback.user_auth.user.User;

@Entity
@Table(
        name = "qaas",
        indexes = {
                @Index(name = "idx_qaas_user", columnList = "user_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qaa extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_qaas_user_id"))
    private User user;

    @Builder
    public Qaa(
            String title,
            String content,
            User user
    ) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.viewCount = 0L;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }
}
