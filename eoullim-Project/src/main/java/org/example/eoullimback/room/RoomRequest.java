package org.example.eoullimback.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.file.RoomFile;

import java.util.List;

public class RoomRequest {

    @Data
    public static class CreateDTO {
        @NotBlank(message = "방 이름은 필수입니다.")
        @Size(max = 150, message = "최대 50자 까지 입력 가능합니다.")
        String name;

        @NotBlank(message = "내용은 필수입니다.")
        String content;

        @NotBlank(message = "상태값은 필수 입니다.")
        RoomStatus status;

        List<RoomFile> roomFile;

        public Room toEntity () {
            return Room.builder()
                    .name(name)
                    .content(content)
                    .status(status)
                    .build();
        }
    }
}
