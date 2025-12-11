package org.example.eoullimback.qaa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(
        name = "qaas",
        indexes = {
                @Index(name = "idx_qaa_user", columnList = "user_id")
        }
)
@Entity
public class Qaa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_qaa_user_id"))
//    private User user;
}
