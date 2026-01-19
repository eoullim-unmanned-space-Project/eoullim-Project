package org.example.eoullimback.review;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.review.admin.AdminReviewListDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;

    // 마이페이지
    public List<ReviewListItemDTO> findList(Long userId, String code) {
        String safeCode = (code == null) ? "" : code.trim();
        return reviewRepository.findMyReviewList(userId, safeCode);
    }

    // 관리자
    public List<AdminReviewListDTO> findAll(String keyword) {
        String safe = (keyword == null) ? "" : keyword.trim();
        return reviewRepository.findAllForAdmin(safe);
    }
}
