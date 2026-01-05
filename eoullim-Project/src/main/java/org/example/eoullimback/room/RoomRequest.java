package org.example.eoullimback.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.place.Place;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class RoomRequest {

    @Data
    public static class CreateDTO {
        @NotBlank(message = "방 이름은 필수입니다.")
        @Size(max = 150, message = "최대 150자 까지 입력 가능합니다.")
        String name;

        @NotBlank(message = "내용은 필수입니다.")
        String content;

        @Min(value = 1, message = "최대 1명 이상이어야 합니다.")
        int maxCapacity;

        @Min(value = 100, message = "값은 100원 부터입니다.")
        int defaultPrice;

        @NotNull(message = "상태값은 필수 입니다.")
        RoomStatus status;

        MultipartFile roomImage;

        public Room toEntity (Place place, String roomImageFileName) {
            return Room.builder()
                    .place(place)
                    .name(name)
                    .content(content)
                    .maxCapacity(maxCapacity)
                    .defaultPrice(defaultPrice)
                    .status(status)
                    .roomImage(roomImageFileName)
                    .build();
        }
    }


    @Data
    public static class UpdateDTO {
        @Size(max = 150, message = "최대 150자 까지 입력 가능합니다.")
        String name;

        @NotBlank(message = "내용은 필수입니다.")
        String content;

        @Min(value = 1, message = "최대 1명 이상이어야 합니다.")
        Integer maxCapacity;

        @Min(value = 100, message = "값은 100원 부터입니다.")
        int defaultPrice;

        @NotNull(message = "상태값은 필수 입니다.")
        RoomStatus status;

        private MultipartFile roomImage;
        private String roomImageFileName;
    }
}
