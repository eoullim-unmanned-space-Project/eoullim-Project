package org.example.eoullimback.notice;

import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.user_auth.user.User;

public interface NoticeService {

    Notice save(NoticeRequest.CreateDTO request, User sessionUser);

    PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> noticeListFindAll(int page, int size, String keyword);

    NoticeResponse.DetailDTO findById(Long id);

    NoticeResponse.UpdateFormDTO update(Long noticeId, NoticeRequest.UpdateDTO request, User sessionUser);

    void delete(Long noticeId, User sessionUser);
}
