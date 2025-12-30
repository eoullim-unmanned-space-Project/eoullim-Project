package org.example.eoullimback.comment;

import java.util.List;

public interface CommentService {

    Comment createComment(CommentRequest.createDTO saveRequest, Long sessionUserId);

    List<CommentResponse.ListDTO> listComment(Long qaaId, Long sessionUserId, Long commentId);

    Long getQaaIdByCommentId(Long commentId);

    CommentResponse.UpdateFormDTO updateComment(CommentRequest.UpdateDTO request, Long commentId, Long sessionUserId);

    Long deleteComment(Long commentId, Long sessionUserId);
}
