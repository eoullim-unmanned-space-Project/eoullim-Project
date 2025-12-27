package org.example.eoullimback.place;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.eoullimback._common.enums.place.Category;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class PlaceRequest {

        @Data
        public static class CreateDTO {
                @NotBlank(message = "장소명은 필수입니다.")
                @Size(max = 50, message = "최대 50자 까지 입력 가능합니다.")
                private String name;

                @NotBlank(message = "주소명은 필수입니다.")
                @Size(max = 50, message = "최대 100자 까지 입력 가능합니다.")
                private String address;

                @NotNull(message = "위도값은 필수입니다.")
                @DecimalMin(value = "-90.0", inclusive = true, message = "위도는 -90 이상이어 합니다.")
                @DecimalMax(value = "90.0", inclusive = true, message = "위도는 90 이상이어 합니다.")
                private BigDecimal latitude;

                @NotNull(message = "경도값은 필수입니다.")
                @DecimalMin(value = "-180.0", inclusive = true, message = "위도는 -180 이상이어 합니다.")
                @DecimalMax(value = "180.0", inclusive = true, message = "위도는 180 이상이어 합니다.")
                private BigDecimal longitude;

                @NotNull(message = "카테고리는 필수 입니다.")
                private Category category;

                private MultipartFile profileImage;

                public Place toEntity (String profileImageFileName) {
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
                @NotBlank(message = "장소명은 필수입니다.")
                @Size(max = 50, message = "최대 50자 까지 입력 가능합니다.")
                private String name;

                @NotNull(message = "카테고리는 필수 입니다.")
                private Category category;

                private MultipartFile profileImage;
                private String profileImageFileName;
        }

}


