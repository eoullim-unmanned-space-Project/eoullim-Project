package org.example.eoullimback.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewSaveRequest(

        @NotNull(message = "평점은 필수입니다.")
        @Min(value = 1, message = "평점은 1이상이어야 합니다.")
        @Max(value = 5, message = "평점은 5이하여야 합니다.")
        Integer rating,

        @NotBlank(message = "리뷰 내용은 필수입니다.")
        String content,

        @NotNull(message = "roomId는 필수입니다.")
        Long roomId,

        @NotNull(message = "paymentId는 필수입니다.")
        Long paymentId
) {
}
