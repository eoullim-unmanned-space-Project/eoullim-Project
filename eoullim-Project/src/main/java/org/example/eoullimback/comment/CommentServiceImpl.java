package org.example.eoullimback.comment;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
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
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final QaaRepository qaaRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 작성
     */
    @Transactional
    @Override
    public Comment createComment(CommentRequest.CreateDTO request, Long sessionUserId) {

        User userEntity = userRepository.findById(sessionUserId)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        Qaa qaaEntity = qaaRepository.findById(request.getQaaId())
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        Comment comment = request.toEntity(userEntity, qaaEntity);
        return commentRepository.save(comment);
    }

    /**
     * 댓글 리스트
     */
    @Override
    public List<CommentResponse.ListDTO> listComment(Long qaaId, Long sessionUserId) {

        List<Comment> commentList = commentRepository.findByQaaIdWithUser(qaaId);
        return commentList.stream()
                .map(comment -> new CommentResponse.ListDTO(comment, sessionUserId))
                .collect(Collectors.toList());
    }

    /**
     * 댓글 수정 화면
     */
    @Transactional
    @Override
    public CommentResponse.UpdateFormDTO updateCommentForm(Long commentId, Long sessionUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new Exception404(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.isOwner(sessionUserId)){
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        return new CommentResponse.UpdateFormDTO(comment);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    @Override
    public CommentResponse.UpdateFormDTO updateComment(CommentRequest.UpdateDTO request, Long commentId, Long sessionUserId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new Exception404(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.isOwner(sessionUserId)) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }
        comment.updateContent(request.getContent());
        return new CommentResponse.UpdateFormDTO(comment);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    @Override
    public Long deleteComment(Long commentId, Long sessionUserId) {
        Comment commentEntity = commentRepository.findByWithUser(commentId)
                .orElseThrow(() -> new Exception404(ErrorCode.COMMENT_NOT_FOUND));

        if(!commentEntity.isOwner(sessionUserId)) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        Long qaaId = commentEntity.getQaa().getId();
        commentRepository.delete(commentEntity);

        return qaaId;
    }
}
