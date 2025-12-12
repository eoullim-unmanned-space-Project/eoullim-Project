package org.example.eoullimback.file;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback.place.Place;

@Entity
@Table(name = "place_files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_place_files_place_files"))
    private Place place;

    @Builder
    public PlaceFile(Long id, Place place) {
        this.id = id;
        this.place = place;
    }
}
