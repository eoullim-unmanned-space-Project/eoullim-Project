package org.example.eoullimback.comment;

import java.util.List;

public interface CommentService {

    Comment createComment(CommentRequest.CreateDTO saveRequest, Long sessionUserId);

    List<CommentResponse.ListDTO> listComment(Long qaaId, Long sessionUserId);

    CommentResponse.UpdateFormDTO updateCommentForm(Long commentId, Long sessionUserId);

    CommentResponse.UpdateFormDTO updateComment(CommentRequest.UpdateDTO request, Long commentId, Long sessionUserId);

    Long deleteComment(Long commentId, Long sessionUserId);
}
