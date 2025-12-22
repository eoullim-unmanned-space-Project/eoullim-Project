package org.example.eoullimback.notice;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.notice.dto.request.NoticeSaveRequest;
import org.example.eoullimback.notice.dto.request.NoticeUpdateRequest;
import org.example.eoullimback.notice.dto.response.NoticeDetailResponse;
import org.example.eoullimback.notice.dto.response.NoticeListResponse;
import org.example.eoullimback.notice.dto.response.NoticeUpdateFormResponse;
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
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public Notice save(NoticeSaveRequest request, User sessionUser) {
        if (sessionUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        Notice notice = request.toEntity(sessionUser);
        noticeRepository.save(notice);
        return notice;
    }

    public PageResponse<NoticeListResponse> noticeListFindAll(int page, int size, String keyword) {
        int validPage = Math.max(0, page);
        int validSize = Math.max(1, Math.min(50, size));

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(validPage, validSize, sort);

        Page<Notice> noticePage;
        if(keyword != null && !keyword.trim().isEmpty()) {
            noticePage = noticeRepository.findByTitleContainingOrContentContaining(keyword.trim(), pageable);
        } else {
            noticePage = noticeRepository.findAllWithUserOrderByCreatedAtDesc(pageable);
        }

        return PageResponse.from(noticePage, NoticeListResponse::new);
    }

    @Transactional
    public NoticeDetailResponse findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공지사항 입니다."));

        return new NoticeDetailResponse(notice);
    }

    public NoticeUpdateFormResponse findUpdateForm(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공지사항 입니다."));

        return new NoticeUpdateFormResponse(notice);
    }

    @Transactional
    public NoticeUpdateFormResponse update(Long noticeId, NoticeUpdateRequest updateRequest, User sessionUser) {

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공지사항 입니다."));

        if (!notice.getUser().getId().equals((sessionUser.getId()))) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        notice.update(updateRequest.title(), updateRequest.content());

        return new NoticeUpdateFormResponse(notice);
    }

    @Transactional
    public void delete(Long noticeId, User sessionUser) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공지사항 입니다."));

        if (!notice.getUser().getId().equals(sessionUser.getId())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        noticeRepository.delete(notice);
    }
}
