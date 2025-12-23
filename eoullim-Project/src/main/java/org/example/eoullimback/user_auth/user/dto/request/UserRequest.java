package org.example.eoullimback.user_auth.user.dto.request;

import lombok.Data;
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
    }
}
