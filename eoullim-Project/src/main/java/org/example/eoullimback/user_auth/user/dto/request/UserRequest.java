package org.example.eoullimback.user_auth.user.dto.request;

import lombok.Data;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception404;
import org.springframework.web.multipart.MultipartFile;

public class UserRequest {

    @Data
    public static class UpDateDTO {
            private String name;
            private String email;
            private MultipartFile userProfile;
            private String useProfileFileName;

            public static UpDateDTO of(UpDateDTO dto, String fileName) {
                UpDateDTO newDTO = new UpDateDTO();
                newDTO.setName(dto.getName());
                newDTO.setEmail(dto.getEmail());
                newDTO.setUserProfile(dto.getUserProfile());
                newDTO.setUseProfileFileName(fileName);
                return newDTO;
        }

        public void validate() {
                if (name == null || name.isEmpty()) {
                    throw new Exception400(ErrorCode.MISSING_PARAMETER);
                }
                if (email == null || email.isEmpty()) {
                    throw new Exception400(ErrorCode.MISSING_EMAIL);
                }
                if (!email.contains("@")) {
                    throw new Exception400(ErrorCode.INVALID_EMAIL_FORMAT);
                }
        }
    }

    @Data
    public static class EmailCheckDTO {
        private String email;
        private String code;

        public void validate() {
            if (email == null || email.trim().isEmpty()) {
                throw new Exception400(ErrorCode.MISSING_EMAIL);
            }
            if (!email.contains("@")) {
                throw new Exception400(ErrorCode.INVALID_EMAIL_FORMAT);
            }
        }
    }

    @Data
    public static class PasswordCheckDTO {
        private String password;

        public void validate() {
            if (password == null || password.trim().isEmpty()) {
                throw new Exception400(ErrorCode.PASSWORD_REQUIRED);
            }
        }
    }
}
