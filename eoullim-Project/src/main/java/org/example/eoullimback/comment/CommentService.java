package org.example.eoullimback.comment;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.comment.dto.request.CommentSaveRequest;
import org.example.eoullimback.comment.dto.request.CommentUpdateRequest;
import org.example.eoullimback.comment.dto.response.CommentListResponse;
import org.example.eoullimback.comment.dto.response.CommentUpdateFormResponse;
import org.example.eoullimback.qaa.Qaa;
import org.example.eoullimback.qaa.QaaRepository;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final QaaRepository qaaRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 작성
     */
    @Transactional
    public Comment createComment(CommentSaveRequest saveRequest, Long sessionUserId) {

        User userEntity = userRepository.findById(sessionUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Qaa qaaEntity = qaaRepository.findById(saveRequest.qaaId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Comment comment = saveRequest.toEntity(userEntity, qaaEntity);
        return commentRepository.save(comment);
    }

    /**
     * 댓글 리스트
     */
    public List<CommentListResponse> listComment(Long qaaId, Long sessionUserId) {

        List<Comment> commentList = commentRepository.findByQaaIdWithUser(qaaId);
        return commentList.stream()
                .map(comment -> new CommentListResponse(comment, sessionUserId))
                .collect(Collectors.toList());
    }

    /**
     * 댓글 수정 화면
     */
    public CommentUpdateFormResponse updateCommentForm(Long commentId, Long sessionUserId) {
        Comment commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if(!commentEntity.isOwner(sessionUserId)){
            throw new RuntimeException("권한이 없습니다.");
        }

        return new CommentUpdateFormResponse(commentEntity);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public CommentUpdateFormResponse updateComment(CommentUpdateRequest updateRequest, Long commentId, Long sessionUserId) {

        Comment commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if(!commentEntity.isOwner(sessionUserId)) {
            throw new RuntimeException("게시물 수정 권한이 없습니다.");
        }
        commentEntity.updateContent(updateRequest.content(), updateRequest.qaaId());
        return new CommentUpdateFormResponse(commentEntity);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public Long deleteComment(Long commentId, Long sessionUserId) {
        Comment commentEntity = commentRepository.findByWithUser(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if(!commentEntity.isOwner(sessionUserId)) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.");
        }

        Long qaaId = commentEntity.getQaa().getId();
        commentRepository.delete(commentEntity);

        return qaaId;
    }
}
