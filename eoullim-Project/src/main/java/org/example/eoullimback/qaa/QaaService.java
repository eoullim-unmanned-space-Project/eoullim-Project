package org.example.eoullimback.qaa;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.qaa.dto.request.QaaSaveRequest;
import org.example.eoullimback.qaa.dto.response.QaaDetailResponse;
import org.example.eoullimback.qaa.dto.response.QaaListResponse;
import org.example.eoullimback.qaa.dto.response.QaaUpdateFormResponse;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QaaService {

    private final QaaRepository qaaRepository;

    @Transactional
    public void save(QaaSaveRequest request, User sessionUser) {
        if (sessionUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        Qaa qaa = request.toEntity(sessionUser);
        qaaRepository.save(qaa);
    }

    public List<QaaListResponse> findAll() {
        return qaaRepository.findAllByOrderByIdDesc()
                .stream()
                .map(QaaListResponse::new)
                .toList();
    }

    @Transactional
    public Object findById(Long id) {
        qaaRepository.increaseViewCount(id);

        Qaa qaa = qaaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Q&A 입니다."));

        return new QaaDetailResponse(qaa);
    }

    public Object findUpdateForm(Long id) {
        Qaa qaa = qaaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Q&A 입니다."));

        return new QaaUpdateFormResponse(qaa);
    }

    @Transactional
    public void delete(Long id) {
        if (!qaaRepository.existsById(id)) {
            throw new RuntimeException("존재하지 않는 Q&A 입니다.");
        }

        qaaRepository.deleteById(id);
    }
}
