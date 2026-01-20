package org.example.eoullimback.room;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback._common.error.exception.Exception500;
import org.example.eoullimback._common.util.FileUtil;
import org.example.eoullimback.place.Place;
import org.example.eoullimback.place.PlaceRepository;
import org.example.eoullimback.place.PlaceResponse;
import org.example.eoullimback.timeslot.TimeSlotRepository;
import org.example.eoullimback.timeslot.TimeSlotServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService{

    private final PlaceRepository placeRepository;
    private final RoomRepository roomRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotServiceImpl timeSlotService;

    @Override
    @Transactional
    public Room createRoom(RoomRequest.CreateDTO request) {

        String roomImageFileName = null;

        Place place = placeRepository.findById(request.placeId)
                .orElseThrow(() -> new Exception404(ErrorCode.PLACE_NOT_FOUND));

        if (request.getRoomImage() != null && !request.getRoomImage().isEmpty()) {
            try {
                if (!FileUtil.isImageFile(request.getRoomImage())) {
                    throw new Exception400(ErrorCode.ONLY_FILE_IMG);
                }

                roomImageFileName = FileUtil.saveFile(request.getRoomImage());
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

        Room room = request.toEntity(place, roomImageFileName);

        Room saveRoom = roomRepository.save(room);

        LocalDate today = LocalDate.now();

        timeSlotService.createRoomInTimeSlots(saveRoom, today);

        return saveRoom;

    }

    @Override
    public List<RoomResponse.ListDTO> roomList(Long placeId) {
        Place placeEntity = placeRepository.findById(placeId)
                .orElseThrow(() -> new Exception404(ErrorCode.PLACE_NOT_FOUND));

        return roomRepository.findByPlaceId(placeEntity.getId())
                .stream()
                .map(RoomResponse.ListDTO::new)
                .toList();
    }

    @Override
    public RoomResponse.DetailDTO detailRoom(Long roomId) {
        Room room =  roomRepository.findById(roomId)
                .orElseThrow(() -> new Exception404(ErrorCode.ROOM_NOT_FOUND));

        return new RoomResponse.DetailDTO(room);
    }

    @Override
    @Transactional
    public Room updateRoom(Long roomId, RoomRequest.UpdateDTO request) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new Exception404(ErrorCode.ROOM_NOT_FOUND));

        String oldRoomImage = room.getRoomImage();
        if (request.getRoomImage() != null && !request.getRoomImage().isEmpty()) {
            if (!FileUtil.isImageFile(request.getRoomImage())) {
                throw new Exception400(ErrorCode.ONLY_FILE_IMG);
            }

            try {
                String newRoomImageFileName = FileUtil.saveFile(request.getRoomImage());
                request.setRoomImageFileName(newRoomImageFileName);

                if (oldRoomImage != null && !oldRoomImage.isEmpty()) {
                    FileUtil.deleteFile(oldRoomImage);
                }
            } catch (IOException e) {
                throw new Exception500(ErrorCode.FILE_SAVE_FAILED);
            }
        } else {
            request.setRoomImageFileName(oldRoomImage);
        }

        room.update(request);

        return room;
    }

    @Override
    @Transactional
    public void deleteRoom(Long roomId) {
        Room room =  roomRepository.findByWithPlace(roomId)
                .orElseThrow(() -> new Exception404(ErrorCode.ROOM_NOT_FOUND));

        timeSlotRepository.deleteAllByRoom(room);

        roomRepository.deleteById(roomId);
    }

    @Override
    public PageResponse.PageDTO<Room, RoomResponse.AdminDetailDTO> roomAdminList(int page, int size, String keyword, RoomStatus status) {
        int validPage = Math.max(0, page);
        int validSize = Math.max(1, Math.min(20, size));

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(validPage, validSize, sort);

        Page<Room> roomPage;

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasStatus = status != null;

        if (hasKeyword && hasStatus) {
            roomPage = roomRepository.searchByKeywordAndStatus(keyword.trim(), status, pageable);
        } else if (hasKeyword) {
            roomPage = roomRepository.searchByKeyword(keyword.trim(), pageable);
        } else if (hasStatus) {
            roomPage = roomRepository.searchByStatus(status, pageable);
        } else {
            roomPage = roomRepository.findAll(pageable);
        }

        return new PageResponse.PageDTO<>(roomPage, RoomResponse.AdminDetailDTO::new);
    }


}
