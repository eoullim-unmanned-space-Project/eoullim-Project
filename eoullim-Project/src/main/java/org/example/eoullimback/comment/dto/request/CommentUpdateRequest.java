package org.example.eoullimback.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.eoullimback.qaa.Qaa;

public record CommentUpdateRequest(

        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content,

        @NotNull(message = "Q&A ID는 필수입니다.")
        Qaa qaaId
) {
}
