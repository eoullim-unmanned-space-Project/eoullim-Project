package org.example.eoullimback.file;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file_infos")
@Entity
public class FileInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String storedName;

    private String contentType;
    private Long fileSize;

    @Column(nullable = false)
    private String filePath;

    @CreationTimestamp
    private Timestamp createdAt;

    @OneToMany(mappedBy = "fileInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlaceFile> placeFiles = new HashSet<>();

    @Builder
    public FileInfo(Long id, String originalName, String storedName, String contentType, Long fileSize, String filePath, Set<PlaceFile> placeFiles) {
        this.id = id;
        this.originalName = originalName;
        this.storedName = storedName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.placeFiles = placeFiles;
    }

    public void addPlaceFile(PlaceFile placeFile) {
        placeFiles.add(placeFile);
        placeFile.setFileInfo(this);
    }
}
