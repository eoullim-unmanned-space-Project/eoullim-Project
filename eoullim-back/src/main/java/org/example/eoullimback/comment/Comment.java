package org.example.eoullimback.comment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.eoullimback.qaa.Qaa;

@Data
@NoArgsConstructor
@Table(
        name = "comments",
        indexes = {
                @Index(name = "idx_comment_user", columnList = "user_id")
        }
)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", columnDefinition = "TEXT", nullable = false)
    private String comment;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_comment_user_id"))
//    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qaa_id", foreignKey = @ForeignKey(name = "fk_comment_qaa_id"))
    private Qaa qaa;
}
