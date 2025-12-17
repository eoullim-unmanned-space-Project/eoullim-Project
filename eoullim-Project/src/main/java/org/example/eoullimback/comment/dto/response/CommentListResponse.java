package org.example.eoullimback.comment.dto.response;

import org.example.eoullimback.comment.Comment;

import java.time.LocalDateTime;

public record CommentListResponse(
        Long id,
        String content,
        String name,
        LocalDateTime createdAt
) {
    public CommentListResponse(Comment comment) {
        this(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName(),
                comment.getCreatedAt()
        );
    }
}
