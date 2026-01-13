package org.example.eoullimback.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;

    public List<ReviewListItemDTO> findList(Long userId, String code) {
        String safeCode = (code == null) ? "" : code.trim();
        return reviewRepository.findMyReviewList(userId, safeCode);
    }
}
