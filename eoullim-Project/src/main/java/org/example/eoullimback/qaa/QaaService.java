package org.example.eoullimback.qaa;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageDTO;
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
public class QaaService {

    private final QaaRepository qaaRepository;

    @Transactional
    public Qaa save(QaaSaveRequest request, User sessionUser) {
        if (sessionUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        Qaa qaa = request.toEntity(sessionUser);
        qaaRepository.save(qaa);
        return qaa;
    }

    public PageDTO<QaaListResponse> qaaListFindAll(int page, int size, String keyword) {
        int validPage = Math.max(0, page);
        int validSize = Math.max(1, Math.min(50, size));

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(validPage, validSize, sort);

        Page<Qaa> qaaPage;
        if(keyword != null && !keyword.trim().isEmpty()) {
            qaaPage = qaaRepository.findByTitleContainingOrContentContaining(keyword.trim(), pageable);
        } else {
            qaaPage = qaaRepository.findAllWithUserOrderByCreatedAtDesc(pageable);
        }

        return PageDTO.from(qaaPage, QaaListResponse::new);
    }

    @Transactional
    public QaaDetailResponse findById(Long id) {
        Qaa qaa = qaaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Q&A 입니다."));

        qaa.increaseViewCount();

        return new QaaDetailResponse(qaa);
    }

    public QaaUpdateFormResponse findUpdateForm(Long id) {
        Qaa qaa = qaaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Q&A 입니다."));

        return new QaaUpdateFormResponse(qaa);
    }

    @Transactional
    public QaaUpdateFormResponse update(Long qaaId, QaaUpdateRequest updateRequest, User sessionUser) {

        Qaa qaa = qaaRepository.findById(qaaId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Q&A 입니다."));

        if (!qaa.getUser().getId().equals(sessionUser.getId())) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        qaa.update(updateRequest.title(), updateRequest.content());

        return new QaaUpdateFormResponse(qaa);
    }

    @Transactional
    public void delete(Long id, User sessionUser) {
        Qaa qaa = qaaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Q&A 입니다."));

        if (!qaa.getUser().getId().equals(sessionUser.getId())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        qaaRepository.delete(qaa);
    }
}
