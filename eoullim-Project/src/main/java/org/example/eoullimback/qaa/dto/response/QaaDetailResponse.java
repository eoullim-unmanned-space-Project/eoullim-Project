package org.example.eoullimback.qaa.dto.response;

import org.example.eoullimback.qaa.Qaa;

import java.time.LocalDateTime;

public record QaaDetailResponse(
        Long id,
        String title,
        String content,
        Long userId,
        String name,
        Long viewCount,
        LocalDateTime createdAt
) {
    public QaaDetailResponse(Qaa qaa) {
        this(
                qaa.getId(),
                qaa.getTitle(),
                qaa.getContent(),
                qaa.getUser() != null ? qaa.getUser().getId() : null,
                qaa.getUser() != null ? qaa.getUser().getName() : null,
                qaa.getViewCount(),
                qaa.getCreatedAt()
        );
    }
}
