package org.example.eoullimback.comment;

import org.example.eoullimback.comment.dto.request.CommentSaveRequest;
import org.example.eoullimback.comment.dto.request.CommentUpdateRequest;
import org.example.eoullimback.comment.dto.response.CommentListResponse;
import org.example.eoullimback.comment.dto.response.CommentUpdateFormResponse;

import java.util.List;

public interface CommentService {

    Comment createComment(CommentSaveRequest saveRequest, Long sessionUserId);

    List<CommentListResponse> listComment(Long qaaId, Long sessionUserId);

    CommentUpdateFormResponse updateCommentForm(Long commentId, Long sessionUserId);

    CommentUpdateFormResponse updateComment(CommentUpdateRequest updateRequest, Long commentId, Long sessionUserId);

    Long deleteComment(Long commentId, Long sessionUserId);
}
