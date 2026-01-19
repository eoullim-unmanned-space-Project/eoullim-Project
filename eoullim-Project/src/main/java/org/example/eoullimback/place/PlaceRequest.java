package org.example.eoullimback.place;

import lombok.Data;
import org.example.eoullimback._common.enums.place.Category;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class PlaceRequest {

    @Data
    public static class CreateDTO {
        private String name;
        private String address;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private Category category;
        private MultipartFile profileImage;

        public void validate() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("장소명을 입력해주세요.");
            }
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalArgumentException("주소를 입력해주세요.");
            }
            if (latitude == null) {
                throw new IllegalArgumentException("위도값은 필수입니다.");
            }
            if (longitude == null) {
                throw new IllegalArgumentException("경도값은 필수입니다.");
            }
            if (category == null) {
                throw new IllegalArgumentException("카테고리는 필수 입니다.");
            }
        }

        public Place toEntity(String profileImageFileName) {
            return Place.builder()
                    .name(name)
                    .address(address)
                    .latitude(latitude)
                    .longitude(longitude)
                    .category(category)
                    .profileImage(profileImageFileName)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {
        private String name;
        private Category category;
        private MultipartFile profileImage;
        private String profileImageFileName;

        public void validate() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("장소명을 입력해주세요.");
            }
            if (category == null) {
                throw new IllegalArgumentException("카테고리는 필수 입니다.");
            }
        }
    }

}


