package org.example.eoullimback.notice;

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

    @Transactional
    @Override
    public Notice save(NoticeRequest.CreateDTO request, User sessionUser) {
        if (sessionUser == null) {
            throw new Exception403(ErrorCode.LOGIN_ONLY);
        }

        Notice notice = request.toEntity(sessionUser);
        noticeRepository.save(notice);
        return notice;
    }

    @Override
    public PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> noticeListFindAll(int page, int size, String keyword) {
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

        return new PageResponse.PageDTO<>(
                noticePage,
                NoticeResponse.ListDTO::new
        );
    }

    @Override
    public NoticeResponse.DetailDTO findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.NOTICE_NOT_FOUND));

        return new NoticeResponse.DetailDTO(notice);
    }

    public NoticeResponse.UpdateFormDTO findUpdateForm(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.NOTICE_NOT_FOUND));

        return new NoticeResponse.UpdateFormDTO(notice);
    }

    @Transactional
    @Override
    public NoticeResponse.UpdateFormDTO update(Long noticeId, NoticeRequest.UpdateDTO request, User sessionUser) {

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new Exception404(ErrorCode.NOTICE_NOT_FOUND));

        if (!notice.getUser().getId().equals((sessionUser.getId()))) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        request.updateEntity(notice);
        return new NoticeResponse.UpdateFormDTO(notice);
    }

    @Transactional
    @Override
    public void delete(Long noticeId, User sessionUser) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new Exception404(ErrorCode.NOTICE_NOT_FOUND));

        if (!notice.getUser().getId().equals(sessionUser.getId())) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        noticeRepository.delete(notice);
    }
}
