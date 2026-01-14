package org.example.eoullimback.notice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
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
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> noticeListFindAll(int page, int size, String keyword) {
        int validPage = Math.max(0, page);
        int validSize = Math.max(1, Math.min(50, size));

        Pageable pageable = PageRequest.of(validPage, validSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Notice> noticePage =
                (keyword != null && !keyword.trim().isEmpty())
                        ? noticeRepository.findByTitleContainingOrContentContaining(keyword.trim(), pageable)
                        : noticeRepository.findAllWithUserOrderByCreatedAtDesc(pageable);

        return new PageResponse.PageDTO<>(noticePage, NoticeResponse.ListDTO::new);
    }

    @Override
    public NoticeResponse.DetailDTO findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.NOTICE_NOT_FOUND));
        return new NoticeResponse.DetailDTO(notice);
    }

    @Override
    public PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> adminNoticeListFindAll(
            User sessionUser, int page, int size, String keyword
    ) {
        assertAdmin(sessionUser);

        int validPage = Math.max(0, page);
        int validSize = Math.max(1, Math.min(50, size));

        Pageable pageable = PageRequest.of(validPage, validSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Notice> noticePage =
                (keyword != null && !keyword.trim().isEmpty())
                        ? noticeRepository.findByTitleContainingOrContentContaining(keyword.trim(), pageable)
                        : noticeRepository.findAllWithUserOrderByCreatedAtDesc(pageable);

        return new PageResponse.PageDTO<>(noticePage, NoticeResponse.ListDTO::new);
    }

    @Override
    public void assertAdmin(User sessionUser) {
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        // if (!sessionUser.isAdmin()) throw new Exception403(ErrorCode.ACCESS_DENIED);
        // if (sessionUser.getRole() != Role.ADMIN) throw new Exception403(ErrorCode.ACCESS_DENIED);

        // throw new Exception403(ErrorCode.ACCESS_DENIED);
    }

    @Transactional
    @Override
    public Notice saveAsAdmin(NoticeRequest.CreateDTO request, User sessionUser) {
        assertAdmin(sessionUser);

        Notice notice = request.toEntity(sessionUser);
        return noticeRepository.save(notice);
    }

    @Override
    public NoticeResponse.UpdateFormDTO findUpdateForm(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new Exception404(ErrorCode.NOTICE_NOT_FOUND));

        return new NoticeResponse.UpdateFormDTO(notice);
    }

    @Transactional
    @Override
    public NoticeResponse.UpdateFormDTO updateAsAdmin(Long noticeId, NoticeRequest.UpdateDTO request, User sessionUser) {
        assertAdmin(sessionUser);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new Exception404(ErrorCode.NOTICE_NOT_FOUND));

        request.updateEntity(notice);
        return new NoticeResponse.UpdateFormDTO(notice);
    }

    @Transactional
    @Override
    public void deleteAsAdmin(Long noticeId, User sessionUser) {
        assertAdmin(sessionUser);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new Exception404(ErrorCode.NOTICE_NOT_FOUND));

        noticeRepository.delete(notice);
    }
}
