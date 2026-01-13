package org.example.eoullimback.qaa;

import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.user_auth.user.User;

public interface QaaService {
    Qaa createQaa(QaaRequest.CreateDTO createDTO, User sessionUser);

    PageResponse.PageDTO<Qaa, QaaResponse.ListDTO> qaaListFindAll(int page, int size, String keyword);

    QaaResponse.DetailDTO qaaDetailResponse(Long id);

    void increaseViewCount(Long id);

    QaaResponse.UpdateFormDTO update(Long qaaId, QaaRequest.UpdateDTO updateRequest, User sessionUser);

    void delete(Long qaaId, User sessionUser);

    QaaResponse.ListPageDTO myQaaList(Long userId, int page, int size);

    PageResponse.PageDTO<Qaa, QaaResponse.ListDTO> adminQaaListFindAll(Long userId, int page, int size, String keyword);


    QaaResponse.UpdateFormDTO findUpdateForm(Long id, Long userId);

    void deleteAsAdmin(Long qaaId, User sessionUser);
}
