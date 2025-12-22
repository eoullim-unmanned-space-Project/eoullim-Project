package org.example.eoullimback.notice.dto.response;

import org.example.eoullimback.notice.Notice;

import java.time.LocalDateTime;

public record NoticeDetailResponse(
        Long id,
        String title,
        String content,
        Long userId,
        String name,
        LocalDateTime createdAt
) {
    public NoticeDetailResponse(Notice notice) {
        this(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getUser() != null ? notice.getUser().getId() : null,
                notice.getUser() != null ? notice.getUser().getName() : null,
                notice.getCreatedAt()
        );
    }
}
