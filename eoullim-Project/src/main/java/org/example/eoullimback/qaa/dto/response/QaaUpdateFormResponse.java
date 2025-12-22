package org.example.eoullimback.qaa.dto.response;

import org.example.eoullimback.qaa.Qaa;

import java.time.LocalDateTime;

public record QaaUpdateFormResponse(
        Long id,
        String title,
        String content,
        String name,
        LocalDateTime updatedAt
) {
    public QaaUpdateFormResponse(Qaa qaa) {
        this(
                qaa.getId(),
                qaa.getTitle(),
                qaa.getContent(),
                qaa.getUser().getName(),
                qaa.getUpdatedAt()
        );
    }

}
