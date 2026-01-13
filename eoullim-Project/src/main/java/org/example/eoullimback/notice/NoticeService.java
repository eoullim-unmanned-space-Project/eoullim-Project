package org.example.eoullimback.notice;

import jakarta.validation.Valid;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.user_auth.user.User;

public interface NoticeService {

    PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> noticeListFindAll(int pageIndex, int size, String keyword);

    NoticeResponse.DetailDTO findById(Long noticeId);

    PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> adminNoticeListFindAll(User sessionUser, int pageIndex, int size, String keyword);

    void assertAdmin(User sessionUser);

    Notice saveAsAdmin(NoticeRequest.@Valid CreateDTO request, User sessionUser);

    NoticeResponse.UpdateFormDTO findUpdateForm(Long noticeId);

    NoticeResponse.UpdateFormDTO updateAsAdmin(Long noticeId, NoticeRequest.UpdateDTO request, User sessionUser);

    void deleteAsAdmin(Long noticeId, User sessionUser);
}
