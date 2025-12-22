package org.example.eoullimback.user_auth.user.dto.request;

import lombok.Builder;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.web.multipart.MultipartFile;

public record UserRequest() {

    @Builder
    public record UpDateDTO(
            String name,
            String email,
            MultipartFile userProfile,
            String useProfileFileName
    ) {

        public static UpDateDTO of(UpDateDTO dto, String fileName) {
            return new UpDateDTO(dto.name(), dto.email(), dto.userProfile(), fileName);
        }
    }
}
