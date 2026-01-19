package org.example.eoullimback.room;

import lombok.Data;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.place.Place;
import org.springframework.web.multipart.MultipartFile;

public class RoomRequest {

    @Data
    public static class CreateDTO {
        Long placeId;
        String name;
        String content;
        int maxCapacity;
        int defaultPrice;
        RoomStatus status;

        MultipartFile roomImage;

        public void validate() {

            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("방명을 입력해주세요.");
            }
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("내용은 필수입니다.");
            }
            if (maxCapacity < 1) {
                throw new IllegalArgumentException("최대 1명 이상이어야 합니다.");
            }
            if (defaultPrice < 100) {
                throw new IllegalArgumentException("값은 100원부터 입니다.");
            }
            if (status == null) {
                throw new IllegalArgumentException("상태값은 필수 입니다.");
            }
        }

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
        String name;
        String content;
        int maxCapacity;
        int defaultPrice;
        RoomStatus status;
        private MultipartFile roomImage;
        private String roomImageFileName;

        public void validate() {

            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("방명을 입력해주세요.");
            }
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("내용은 필수입니다.");
            }
            if (maxCapacity < 1) {
                throw new IllegalArgumentException("최대 1명 이상이어야 합니다.");
            }
            if (defaultPrice < 100) {
                throw new IllegalArgumentException("값은 100원부터 입니다.");
            }
            if (status == null) {
                throw new IllegalArgumentException("상태값은 필수 입니다.");
            }
        }
    }
}
