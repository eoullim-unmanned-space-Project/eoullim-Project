package org.example.eoullimback.qaa;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback.comment.Comment;
import org.example.eoullimback.user_auth.user.User;

import java.util.List;

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

    @OneToMany(mappedBy = "qaa")
    private List<Comment> commentList;

    @Builder
    public Qaa(
            String title,
            String content,
            Long viewCount,
            User user
    ) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.user = user;
    }
}
