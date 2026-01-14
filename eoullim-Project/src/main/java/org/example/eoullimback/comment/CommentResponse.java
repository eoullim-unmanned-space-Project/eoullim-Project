package org.example.eoullimback.comment;

import lombok.Data;
import org.example.eoullimback._common.util.DateTimeUtil;

public class CommentResponse {

    @Data
    public static class ListDTO {

        private Long id;
        private String content;
        private String name;
        private String createdAt;
        private boolean isEditing;
        private boolean isOwner;

        public ListDTO(Comment comment, Long sessionUserId, Long editingCommentId) {
            this(comment, sessionUserId, editingCommentId, false);
        }

        public ListDTO(Comment comment, Long sessionUserId, Long editingCommentId, boolean isAdmin) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.name = comment.getUser().getName();
            this.createdAt = DateTimeUtil.toKstString(comment.getCreatedAt());
            this.isEditing = editingCommentId != null && comment.getId().equals(editingCommentId);

            this.isOwner = isAdmin || comment.isOwner(sessionUserId);
        }
    }
}
