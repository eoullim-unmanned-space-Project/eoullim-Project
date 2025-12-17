package org.example.eoullimback.qaa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QaaUpdateRequest(

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 50, message = "제목은 50자 이내여야 합니다.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        String Content
) {
}
