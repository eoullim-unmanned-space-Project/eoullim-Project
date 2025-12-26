package org.example.eoullimback.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.nio.file.Path;

@Getter
@AllArgsConstructor
public class FileInfo {
    private String originalName;
    private String storedName;
    private String contentType;
    private Long fileSize;
    private Path filePath;


    @Builder
    public FileInfo(String contentType, Path filePath, Long fileSize, String originalName, String storedName) {
        this.contentType = contentType;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.originalName = originalName;
        this.storedName = storedName;
    }
}