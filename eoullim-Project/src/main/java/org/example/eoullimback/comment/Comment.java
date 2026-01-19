package org.example.eoullimback.comment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback.qna.Qna;
import org.example.eoullimback.user_auth.user.User;

@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(name = "idx_comments_user", columnList = "user_id"),
                @Index(name = "idx_comments_qna", columnList = "qna_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_comments_user_id"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qna_id", foreignKey = @ForeignKey(name = "fk_comments_qna_id"))
    private Qna qna;

    @Builder
    public Comment(
            String content,
            User user,
            Qna qna
    ) {
        this.content = content;
        this.user = user;
        this.qna = qna;
    }

    public boolean isOwner(Long userId) {
        if(this.user == null || userId == null) {
            return false;
        }
        Long replyUserId = this.user.getId();
        if(replyUserId == null) {
            return false;
        }
        boolean result =  replyUserId.equals(userId);
        return result;
    }
}
