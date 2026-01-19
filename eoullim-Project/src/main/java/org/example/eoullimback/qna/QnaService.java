package org.example.eoullimback.qna;

import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.user_auth.user.User;

public interface QnaService {
    Qna createQna(QnaRequest.CreateDTO createDTO, User sessionUser);

    PageResponse.PageDTO<Qna, QnaResponse.ListDTO> qnaListFindAll(int page, int size, String keyword);

    QnaResponse.DetailDTO qnaDetailResponse(Long id);

    void increaseViewCount(Long id);

    QnaResponse.UpdateFormDTO update(Long qaaId, QnaRequest.UpdateDTO updateRequest, User sessionUser);

    void delete(Long qnaId, User sessionUser);

    QnaResponse.ListPageDTO myQnaList(Long userId, int page, int size);

    PageResponse.PageDTO<Qna, QnaResponse.ListDTO> adminQnaListFindAll(Long userId, int page, int size, String keyword);

    QnaResponse.UpdateFormDTO findUpdateForm(Long id, Long userId);

    void deleteAsAdmin(Long qnaId, User sessionUser);
}
