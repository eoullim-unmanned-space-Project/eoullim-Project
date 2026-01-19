package org.example.eoullimback.qna;


import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.comment.CommentRepository;
import org.example.eoullimback.geminichatbot.GeminiService;
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
public class QnaServiceImpl implements QnaService {

    private final QnaRepository qnaRepository;
    private final CommentRepository commentRepository;
    private final GeminiService geminiService;

    @Transactional
    @Override
    public Qna createQna(QnaRequest.CreateDTO request, User sessionUser) {
        if (sessionUser == null) {
            throw new Exception403(ErrorCode.LOGIN_ONLY);
        }

        String checkRequest = geminiService.checkQnaTitleAndContent(request.getTitle(), request.getContent());

        if ("BAD".equals(checkRequest) || checkRequest.toUpperCase().contains("BAD")) {
            throw new Exception400(ErrorCode.BAD_CONTENT);
        }

        Qna qna = request.toEntity(sessionUser);
        qnaRepository.save(qna);
        return qna;
    }

    @Override
    public PageResponse.PageDTO<Qna, QnaResponse.ListDTO> qnaListFindAll(int page, int size, String keyword) {
        int validPage = Math.max(0, page);
        int validSize = Math.max(1, Math.min(50, size)
        );

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(validPage, validSize, sort);

        Page<Qna> qnaPage;
        if(keyword != null && !keyword.trim().isEmpty()) {
            qnaPage = qnaRepository.findByTitleContainingOrContentContaining(keyword.trim(), pageable);
        } else {
            qnaPage = qnaRepository.findAllWithUserOrderByCreatedAtDesc(pageable);
        }
        return new PageResponse.PageDTO<>(
                qnaPage,
                QnaResponse.ListDTO::new
        );
    }

    @Override
    public QnaResponse.DetailDTO qnaDetailResponse(Long qnaId) {
        Qna qna = qnaRepository.findByIdWithUser(qnaId)
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        return new QnaResponse.DetailDTO(qna);
    }

    @Transactional
    @Override
    public void increaseViewCount(Long id) {
        qnaRepository.increaseViewCount(id);
    }

    public QnaResponse.UpdateFormDTO findUpdateForm(Long qnaId, Long sessionUserId) {
        Qna qna = qnaRepository.findByIdWithUser(qnaId)
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        if(!qna.isOwner(sessionUserId)){
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        return new QnaResponse.UpdateFormDTO(qna);
    }

    @Transactional
    @Override
    public QnaResponse.UpdateFormDTO update(Long qnaId, QnaRequest.UpdateDTO updateRequest, User sessionUser) {

        Qna qna = qnaRepository.findByIdWithUser(qnaId)
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        if (!qna.getUser().getId().equals(sessionUser.getId())) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        updateRequest.updateEntity(qna);
        return new QnaResponse.UpdateFormDTO(qna);
    }

    @Transactional
    @Override
    public void delete(Long qnaId, User sessionUser) {
        Qna qna = qnaRepository.findByIdWithUser(qnaId)
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        if (!qna.getUser().getId().equals(sessionUser.getId())) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.deleteByQnaId(qnaId);
        qnaRepository.delete(qna);
    }

    @Override
    public QnaResponse.ListPageDTO myQnaList(Long userId, int page, int size) {
        int validPage = Math.max(0, page);
        int validSize = Math.max(1, Math.min(50, size));

        Pageable pageable = PageRequest.of(
                validPage,
                validSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        List<Qna> qnaList = qnaRepository.findMyQnaList(userId, pageable).getContent();

        List<QnaResponse.ListDTO> dtoList = qnaList.stream()
                .map(QnaResponse.ListDTO::new)
                .collect(Collectors.toList());

        return new QnaResponse.ListPageDTO(dtoList);
    }

    @Override
    public PageResponse.PageDTO<Qna, QnaResponse.ListDTO> adminQnaListFindAll(Long userId, int page, int size, String keyword) {
        int validPage = Math.max(0, page);
        int validSize = Math.max(1, Math.min(50, size)
        );

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(validPage, validSize, sort);

        Page<Qna> qnaPage;
        if(keyword != null && !keyword.trim().isEmpty()) {
            qnaPage = qnaRepository.findByTitleContainingOrContentContaining(keyword.trim(), pageable);
        } else {
            qnaPage = qnaRepository.findAllWithUserOrderByCreatedAtDesc(pageable);
        }
        return new PageResponse.PageDTO<>(
                qnaPage,
                QnaResponse.ListDTO::new
        );
    }

    @Transactional
    @Override
    public void deleteAsAdmin(Long qnaId, User sessionUser) {
        if (sessionUser == null) {
            throw new Exception403(ErrorCode.LOGIN_ONLY);
        }

        Qna qna = qnaRepository.findByIdWithUser(qnaId)
                .orElseThrow(() -> new Exception404(ErrorCode.QAA_NOT_FOUND));

        commentRepository.deleteByQnaId(qnaId);
        qnaRepository.delete(qna);
    }
}
