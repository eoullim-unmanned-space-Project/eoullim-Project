package org.example.eoullimback.place.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.eoullimback._common.enums.place.PlaceStatus;

public record SavePlaceRequestDTO(
        @NotBlank(message = "장소명은 필수입니다.")
        @Size(max = 50, message = "최대 50자 까지 입력 가능합니다.")
        String name,

        @NotBlank(message = "주소명은 필수입니다.")
        @Size(max = 50, message = "최대 100자 까지 입력 가능합니다.")
        String address,

        @NotBlank(message = "상태값은 필수입니다.")
        PlaceStatus status
) {}
