package org.example.eoullimback.comment;

import java.util.List;

public interface CommentService {

    Comment createCommentAsAdmin(CommentRequest.createDTO request, Long adminUserId);

    List<CommentResponse.ListDTO> listComment(Long qnaId, Long sessionUserId, Long commentId);

    Long deleteCommentAsAdmin(Long commentId, Long adminUserId);
}
