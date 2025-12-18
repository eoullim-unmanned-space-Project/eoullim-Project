package org.example.eoullimback.comment.dto.response;

import org.example.eoullimback.comment.Comment;

import java.time.LocalDateTime;

public record CommentListResponse(
        Long id,
        String content,
        String name,
        LocalDateTime createdAt,
        boolean isOwner
) {
    public CommentListResponse(Comment comment, Long sessionUserId) {
        this(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName(),
                comment.getCreatedAt(),
                comment.isOwner(sessionUserId)
        );
    }
}
