package org.example.eoullimback.notice.dto.response;

import org.example.eoullimback.notice.Notice;

import java.time.LocalDateTime;

public record NoticeUpdateFormResponse(
        Long id,
        String title,
        String content,
        String name,
        LocalDateTime updatedAt
) {
    public NoticeUpdateFormResponse(Notice notice) {
        this(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getUser().getName(),
                notice.getUpdatedAt()
        );
    }
}
