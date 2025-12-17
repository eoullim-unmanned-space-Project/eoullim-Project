package org.example.eoullimback.comment.dto.response;

import org.example.eoullimback.comment.Comment;

import java.time.LocalDateTime;

public record CommentUpdateFormResponse(
        Long id,
        String content,
        String name,
        LocalDateTime updatedAt
) {
    public CommentUpdateFormResponse(Comment comment) {
        this(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName(),
                comment.getUpdatedAt()
        );
    }
}
