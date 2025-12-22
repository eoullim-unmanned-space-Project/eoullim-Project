package org.example.eoullimback.qaa;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.comment.CommentRepository;
import org.example.eoullimback.qaa.dto.request.QaaSaveRequest;
import org.example.eoullimback.qaa.dto.request.QaaUpdateRequest;
import org.example.eoullimback.qaa.dto.response.QaaDetailResponse;
import org.example.eoullimback.qaa.dto.response.QaaListResponse;
import org.example.eoullimback.qaa.dto.response.QaaUpdateFormResponse;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QaaServiceImpl implements QaaService {

    private final QaaRepository qaaRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Qaa createQaa(QaaSaveRequest request, User sessionUser) {
        if (sessionUser == null) {
            throw new Exception403(ErrorCode.LOGIN_ONLY);
        }
        Qaa qaa = request.toEntity(sessionUser);
        qaaRepository.save(qaa);
        return qaa;
    }

    public PageResponse<QaaListResponse> qaaListFindAll(int page, int size, String keyword) {
        int validPage = Math.max(0, page);
        int validSize = Math.max(1, Math.min(50, size)
        );

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(validPage, validSize, sort);

        Page<Qaa> qaaPage;
        if(keyword != null && !keyword.trim().isEmpty()) {
            qaaPage = qaaRepository.findByTitleContainingOrContentContaining(keyword.trim(), pageable);
        } else {
            qaaPage = qaaRepository.findAllWithUserOrderByCreatedAtDesc(pageable);
        }
        return PageResponse.from(qaaPage, QaaListResponse::new);
    }

    @Override
    public QaaDetailResponse qaaDetailResponse(Long qaaId) {
        Qaa qaa = qaaRepository.findByIdWithUser(qaaId)
                .orElseThrow(() -> new Exception404(ErrorCode.ACCESS_DENIED));

        return new QaaDetailResponse(qaa);
    }

    @Transactional
    @Override
    public void increaseViewCount(Long id) {
        qaaRepository.increaseViewCount(id);
    }

    public QaaUpdateFormResponse findUpdateForm(Long qaaId, Long sessionUserId) {
        Qaa qaa = qaaRepository.findByIdWithUser(qaaId)
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        if(!qaa.isOwner(sessionUserId)){
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        return new QaaUpdateFormResponse(qaa);
    }

    @Transactional
    @Override
    public QaaUpdateFormResponse update(Long qaaId, QaaUpdateRequest updateRequest, User sessionUser) {

        Qaa qaa = qaaRepository.findByIdWithUser(qaaId)
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        if (!qaa.getUser().getId().equals(sessionUser.getId())) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        qaa.update(updateRequest.title(), updateRequest.content());
        return new QaaUpdateFormResponse(qaa);
    }

    @Transactional
    @Override
    public void delete(Long qaaId, User sessionUser) {
        Qaa qaa = qaaRepository.findByIdWithUser(qaaId)
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        if (!qaa.getUser().getId().equals(sessionUser.getId())) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.deleteByQaaId(qaaId);
        qaaRepository.delete(qaa);
    }
}
