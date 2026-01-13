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

    @Transactional
    @Override
    public Comment createCommentAsAdmin(CommentRequest.createDTO request, Long adminUserId) {

//        if (!isAdmin(adminUserId)) throw new Exception403(ErrorCode.ACCESS_DENIED);

        User userEntity = userRepository.findById(adminUserId)
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
    public List<CommentResponse.ListDTO> listComment(Long qaaId, Long sessionUserId, Long commentId) {

        List<Comment> commentList = commentRepository.findByQaaIdWithUser(qaaId);
        return commentList.stream()
                .map(comment -> new CommentResponse.ListDTO(comment, sessionUserId, commentId))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Long deleteCommentAsAdmin(Long commentId, Long adminUserId) {

//         if (!isAdmin(adminUserId)) throw new Exception403(ErrorCode.ACCESS_DENIED);

        Comment commentEntity = commentRepository.findByWithUser(commentId)
                .orElseThrow(() -> new Exception404(ErrorCode.COMMENT_NOT_FOUND));

        Long qaaId = commentEntity.getQaa().getId();
        commentRepository.delete(commentEntity);

        return qaaId;
    }
}
