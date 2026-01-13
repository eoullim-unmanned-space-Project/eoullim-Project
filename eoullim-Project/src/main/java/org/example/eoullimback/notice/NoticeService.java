package org.example.eoullimback.notice;

import jakarta.validation.Valid;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.user_auth.user.User;

public interface NoticeService {
    Notice save(NoticeRequest.@Valid CreateDTO request, User sessionUser);

    PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> noticeListFindAll(int pageIndex, int size, String keyword);

    NoticeResponse.DetailDTO findById(Long id);

    Object findUpdateForm(Long id);

    NoticeResponse.UpdateFormDTO update(Long id, NoticeRequest.UpdateDTO request, User sessionUser);

    void delete(Long id, User sessionUser);

    PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> adminNoticeListFindAll(User sessionUser, int pageIndex, int size, String keyword);
}
