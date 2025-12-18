package org.example.eoullimback.room.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.eoullimback._common.enums.room.CapacityPolicy;
import org.example.eoullimback._common.enums.room.Category;
import org.example.eoullimback.file.RoomFile;

import java.util.List;

public record SaveRoomRequestDTO(
        @NotBlank(message = "방 이름은 필수입니다.")
        @Size(max = 150, message = "최대 50자 까지 입력 가능합니다.")
        String name,

        @NotBlank(message = "내용은 필수입니다.")
        String content,

        @NotBlank(message = "카테고리는 필수입니다.")
        Category status,

        @NotBlank(message = "가능 여부는 필수입니다.")
        CapacityPolicy capacityPolicy,

        List<RoomFile> roomFile
) {}
