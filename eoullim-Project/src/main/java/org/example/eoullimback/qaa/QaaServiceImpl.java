package org.example.eoullimback.qaa;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.comment.CommentRepository;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QaaServiceImpl implements QaaService {

    private final QaaRepository qaaRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Qaa createQaa(QaaRequest.CreateDTO request, User sessionUser) {
        if (sessionUser == null) {
            throw new Exception403(ErrorCode.LOGIN_ONLY);
        }

        Qaa qaa = request.toEntity(sessionUser);
        qaaRepository.save(qaa);
        return qaa;
    }

    public PageResponse.PageDTO<Qaa, QaaResponse.ListDTO> qaaListFindAll(int page, int size, String keyword) {
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
        return new PageResponse.PageDTO<>(
                qaaPage,
                QaaResponse.ListDTO::new
        );
    }

    @Override
    public QaaResponse.DetailDTO qaaDetailResponse(Long qaaId) {
        Qaa qaa = qaaRepository.findByIdWithUser(qaaId)
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        return new QaaResponse.DetailDTO(qaa);
    }

    @Transactional
    @Override
    public void increaseViewCount(Long id) {
        qaaRepository.increaseViewCount(id);
    }

    public QaaResponse.UpdateFormDTO findUpdateForm(Long qaaId, Long sessionUserId) {
        Qaa qaa = qaaRepository.findByIdWithUser(qaaId)
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        if(!qaa.isOwner(sessionUserId)){
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        return new QaaResponse.UpdateFormDTO(qaa);
    }

    @Transactional
    @Override
    public QaaResponse.UpdateFormDTO update(Long qaaId, QaaRequest.UpdateDTO updateRequest, User sessionUser) {

        Qaa qaa = qaaRepository.findByIdWithUser(qaaId)
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        if (!qaa.getUser().getId().equals(sessionUser.getId())) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        updateRequest.updateEntity(qaa);
        return new QaaResponse.UpdateFormDTO(qaa);
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

    public QaaResponse.ListPageDTO myQaaList(Long userId, int page, int size) {
        int validPage = Math.max(0, page);
        int validSize = Math.max(1, Math.min(50, size));

        Pageable pageable = PageRequest.of(
                validPage,
                validSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        List<Qaa> qaaList = qaaRepository.findMyQaaList(userId, pageable).getContent();

        List<QaaResponse.ListDTO> dtoList = qaaList.stream()
                .map(QaaResponse.ListDTO::new)
                .collect(Collectors.toList());

        return new QaaResponse.ListPageDTO(dtoList);
    }
}
