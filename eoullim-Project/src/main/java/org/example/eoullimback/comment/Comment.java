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
                @Index(name = "idx_comment_user", columnList = "user_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", columnDefinition = "TEXT", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_comment_user_id"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qaa_id", foreignKey = @ForeignKey(name = "fk_comment_qaa_id"))
    private Qaa qaa;

    @Builder
    public Comment(
            String comment,
            User user,
            Qaa qaa
    ) {
        this.comment = comment;
        this.user = user;
        this.qaa = qaa;
    }
}
