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
            foreignKey = @ForeignKey(name = "fk_place_files_place"))
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false, foreignKey = @ForeignKey(name = "fk_place_file_info"))
    private FileInfo fileInfo;

    @Column(name = "display_order")
    private Integer displayOrder = 0;



    @Builder
    public PlaceFile(Place place, FileInfo fileInfo, Integer displayOrder) {
        this.place = place;
        this.fileInfo = fileInfo;
        this.displayOrder = displayOrder !=null ? displayOrder : 0;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
