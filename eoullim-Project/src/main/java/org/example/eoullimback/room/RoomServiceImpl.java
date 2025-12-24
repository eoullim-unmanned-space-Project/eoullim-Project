package org.example.eoullimback.room;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback._common.util.FileUtil;
import org.example.eoullimback.file.FileInfo;
import org.example.eoullimback.file.RoomFile;
import org.example.eoullimback.place.Place;
import org.example.eoullimback.place.PlaceRepository;
import org.example.eoullimback.room_image.RoomImage;
import org.example.eoullimback.room_image.RoomImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService{

    private final PlaceRepository placeRepository;
    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;

    /**
     * 룸 생성 로직 + 이미지 추가
     */
    @Override
    @Transactional
    public Room createRoom(Long placeId, RoomRequest.@Valid CreateDTO createDTO) throws IOException {

        // 장소 조회
        Place placeEntity = placeRepository.findById(placeId)
                .orElseThrow(() -> new Exception404(ErrorCode.PLACE_NOT_FOUND));

        if (createDTO.roomImage == null || createDTO.roomImage.isEmpty()) {
            throw new Exception404(ErrorCode.MISSING_PARAMETER);
        }

        final int MAX_IMAGE_SIZE = 5;

        if (createDTO.roomImage.size() != MAX_IMAGE_SIZE) {
            throw new Exception400(ErrorCode.MAX_FILE_IMG);
        }

        // 방 생성
        Room room = createDTO.toEntity(placeEntity);

        roomRepository.save(room);

        int displayOrder = 1;
        for (MultipartFile file : createDTO.getRoomImage()) {
            if (file != null && !file.isEmpty()) {
                FileInfo fileInfo = FileUtil.saveRoomFile(file);

                RoomImage roomImage = RoomImage.builder()
                        .contentType(fileInfo.getContentType())
                        .displayOrder(displayOrder++)
                        .filePath(fileInfo.getFilePath().toString())
                        .fileSize(fileInfo.getFileSize())
                        .originalName(fileInfo.getOriginalName())
                        .storedName(fileInfo.getStoredName())
                        .room(room)
                        .build();

                roomImageRepository.save(roomImage);
            }
        }

        return room;
    }

    /**
     * 룸 삭제 + 이미지 같이 삭제
     */
    @Override
    @Transactional
    public void deleteRoom(Long roomId) throws IOException {

        // 1. 방 확인
        Room roomEntity = roomRepository.findById(roomId)
                .orElseThrow(() -> new Exception404(ErrorCode.ROOM_NOT_FOUND));

        // 2. 방에있는 roomImage 전부 가져오기
        List<RoomImage> roomImageEntity = roomImageRepository.findAllByRoom(roomEntity);

        // 3. for문을 돌려서 실제 디렉토리에 있는 값 삭제
        for (RoomImage images : roomImageEntity) {
            FileUtil.deleteFile(images.getFilePath());
        }
        
        // 4. image전부 삭제
        roomImageRepository.deleteAll(roomImageEntity);
        
        // 5. room도 삭제
        roomRepository.delete(roomEntity);
    }

    /**
     * 룸 수정 
     * 제목, 내용, 상태, 초기 값, 상태만 가능
     */
    @Override
    @Transactional
    public Room updateRoom(Long roomId, RoomRequest.@Valid UpdateDTO updateDTO) throws IOException {

        // 1. 방 조회
        Room roomEntity = roomRepository.findById(roomId)
                .orElseThrow(() -> new Exception404(ErrorCode.ROOM_NOT_FOUND));

        // 2. 파일 이미지 삭제
        List<Long> imageIds = updateDTO.getRoomImageIds();
        if (imageIds != null && !imageIds.isEmpty()) {
            for (Long imageId : imageIds) {

                RoomImage roomImageEntity = roomImageRepository.findById(imageId)
                        .orElseThrow(() -> new Exception404(ErrorCode.ROOM_IMG_NOT_FOUND));

                // 룸의 소유인지 확인
                if (!roomImageEntity.checkOwner(roomId)) {
                    throw new Exception403(ErrorCode.ACCESS_DENIED);
                }

                // 디렉토리에 있는 파일을 삭제
                FileUtil.deleteFile(roomImageEntity.getFilePath());
                
                // DB에서 삭제
                roomImageRepository.deleteById(imageId);
            }
        }

        List<MultipartFile> roomImages = updateDTO.roomImages;

        // displayOrder max 값을 들고와준다.
        int maxDisplayOrder = roomImageRepository.findMaxDisplayOrder(roomEntity.getId()) + 1;

        if (roomImages != null && !roomImages.isEmpty()) {

            for (MultipartFile file : roomImages) {
                if (file != null && !file.isEmpty()) {

                    FileInfo fileInfo = FileUtil.saveRoomFile(file);

                    RoomImage roomImage = RoomImage.builder()
                            .originalName(fileInfo.getOriginalName())
                            .storedName(fileInfo.getStoredName())
                            .contentType(fileInfo.getContentType())
                            .fileSize(fileInfo.getFileSize())
                            .filePath(fileInfo.getFilePath().toString())
                            .displayOrder(maxDisplayOrder++)
                            .room(roomEntity)
                            .build();

                    roomImageRepository.save(roomImage);
                }
            }
        }

        roomEntity.update(updateDTO);

        return roomEntity;
    }
}
