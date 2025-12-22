package org.example.eoullimback.file;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@Table(name = "file_infos")
@Entity
public class FileInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;
    private String storedName;
    private String contentType;
    private Long fileSize;
    private String filePath;

    @CreationTimestamp
    private Timestamp createdAt;

    public FileInfo(Long id, String originalName, String storedName,
                    String contentType, Long fileSize, String filePath, Timestamp createdAt
    ) {
        this.id = id;
        this.originalName = originalName;
        this.storedName = storedName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.createdAt = createdAt;
    }
}