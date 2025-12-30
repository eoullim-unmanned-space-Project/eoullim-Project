package org.example.eoullimback.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.file.RoomFile;
import org.example.eoullimback.place.Place;
import org.springframework.beans.factory.annotation.Value;
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

        @Size(min = 100, message = "값은 100원 부터입니다.")
        int defaultPrice;

        @NotNull(message = "상태값은 필수 입니다.")
        RoomStatus status;

        @NotNull(message = "이미지는 필수 입니다.")
        List<MultipartFile> roomImage;

        public Room toEntity (Place place) {
            return Room.builder()
                    .place(place)
                    .name(name)
                    .content(content)
                    .defaultPrice(defaultPrice)
                    .status(status)
                    .build();
        }
    }


    @Data
    public static class UpdateDTO {
        @Size(max = 150, message = "최대 150자 까지 입력 가능합니다.")
        String name;

        String content;

        @Size(min = 100, message = "값은 100원 부터입니다.")
        int defaultPrice;

        RoomStatus status;

        List<MultipartFile> roomImages;

        List<Long> roomImageIds;

        public void validate() {

            if (name != null && name.trim().isEmpty()) {
                throw new IllegalArgumentException("제목은 공백일 수 없습니다.");
            }

            if (content != null && content.trim().isEmpty()) {
                throw new IllegalArgumentException("내용은 공백일 수 없습니다.");
            }
        }
    }
}
