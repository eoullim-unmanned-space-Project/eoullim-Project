package org.example.eoullimback.qaa.dto.response;

import org.example.eoullimback.qaa.Qaa;

import java.time.LocalDateTime;

public record QaaListResponse(
        Long id,
        String title,
        String name,
        Long viewCount,
        LocalDateTime createdAt
) {
    public QaaListResponse(Qaa qaa) {
        this(
                qaa.getId(),
                qaa.getTitle(),
                qaa.getUser() != null ? qaa.getUser().getName() : null,
                qaa.getViewCount(),
                qaa.getCreatedAt()
        );
    }
}
