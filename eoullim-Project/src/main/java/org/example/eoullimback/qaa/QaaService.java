package org.example.eoullimback.qaa;

import org.example.eoullimback.qaa.dto.request.QaaSaveRequest;
import org.example.eoullimback.qaa.dto.request.QaaUpdateRequest;
import org.example.eoullimback.qaa.dto.response.QaaDetailResponse;
import org.example.eoullimback.qaa.dto.response.QaaUpdateFormResponse;
import org.example.eoullimback.user_auth.user.User;

public interface QaaService {
    Qaa createQaa(QaaSaveRequest request, User sessionUser);

    QaaDetailResponse qaaDetailResponse(Long id);

    void increaseViewCount(Long id);

    QaaUpdateFormResponse update(Long qaaId, QaaUpdateRequest updateRequest, User sessionUser);

    void delete(Long qaaId, User sessionUser);
}
