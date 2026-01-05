package org.example.eoullimback.place;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback._common.error.exception.Exception500;
import org.example.eoullimback._common.util.FileUtil;
import org.example.eoullimback.room.*;
import org.example.eoullimback.room_image.RoomImageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService{
    private final PlaceRepository placeRepository;
    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;

    @Override
    @Transactional
    public Place placeCreate(PlaceRequest.CreateDTO request) {
        String profileImageFileName = null;

        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            try {
                if (!FileUtil.isImageFile((request.getProfileImage()))) {
                    throw new Exception400(ErrorCode.ONLY_FILE_IMG);
                }

                profileImageFileName = FileUtil.saveFile(request.getProfileImage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Place place = request.toEntity(profileImageFileName);

        return placeRepository.save(place);
    }

    @Override
    public PageResponse.PageDTO<Place, PlaceResponse.ListDTO> placeList(int page, int size, String keyword) {
        int validPage = Math.max(0, page);
        int validSize = Math.max(1, Math.min(20, size));

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(validPage, validSize, sort);

        Page<Place> placePage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            placePage = placeRepository.searchByKeyword(keyword.trim(), pageable);
        } else {
            placePage = placeRepository.findAll(pageable);
        }

        return new PageResponse.PageDTO<>(placePage, PlaceResponse.ListDTO::new);
    }

    @Override
    public List<PlaceResponse.ListDTO> newPlace() {
        List<PlaceResponse.ListDTO> newPlace = placeRepository.findLatest4Places().stream()
                .map(PlaceResponse.ListDTO::new)
                .toList();

        return newPlace;
    }

    @Override
    public Place placeUpdateForm(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new Exception404(ErrorCode.PLACE_NOT_FOUND));

        return place;
    }

    @Override
    @Transactional
    public Place placeUpdate(Long placeId, PlaceRequest.UpdateDTO request) {

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new Exception404(ErrorCode.PLACE_NOT_FOUND));

        String oldProfileImage = place.getProfileImage();
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            if (!FileUtil.isImageFile(request.getProfileImage())) {
                throw new Exception400(ErrorCode.ONLY_FILE_IMG);
            }

            try {
                String newProfileImageFileName = FileUtil.saveFile(request.getProfileImage());
                request.setProfileImageFileName(newProfileImageFileName);

                if (oldProfileImage != null && !oldProfileImage.isEmpty()) {
                    FileUtil.deleteFile(oldProfileImage);
                }
            } catch (IOException e) {
                throw new Exception500(ErrorCode.FILE_SAVE_FAILED);
            }
        } else {
            request.setProfileImageFileName(oldProfileImage);
        }

        place.update(request);

        return place;
    }

    @Override
    @Transactional
    public void placeDelete(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new Exception404(ErrorCode.PLACE_NOT_FOUND));

        roomRepository.deleteById(placeId);

        placeRepository.deleteById(placeId);
    }
}
