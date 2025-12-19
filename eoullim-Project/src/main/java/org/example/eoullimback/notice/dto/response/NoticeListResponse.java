package org.example.eoullimback.notice.dto.response;

import org.example.eoullimback.notice.Notice;

import java.time.LocalDateTime;

public record NoticeListResponse(
        Long id,
        String title,
        String name,
        LocalDateTime createdAt
) {
    public NoticeListResponse(Notice notice) {
        this(
                notice.getId(),
                notice.getTitle(),
                notice.getUser() != null ? notice.getUser().getName() : null,
                notice.getCreatedAt()
        );
    }
}
