package org.example.eoullimback.comment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback.qaa.Qaa;
import org.example.eoullimback.user_auth.user.User;

@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(name = "idx_comments_user", columnList = "user_id"),
                @Index(name = "idx_comments_qaa", columnList = "qaa_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_comments_user_id"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qaa_id", foreignKey = @ForeignKey(name = "fk_comments_qaa_id"))
    private Qaa qaa;

    @Builder
    public Comment(
            String content,
            User user,
            Qaa qaa
    ) {
        this.content = content;
        this.user = user;
        this.qaa = qaa;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
