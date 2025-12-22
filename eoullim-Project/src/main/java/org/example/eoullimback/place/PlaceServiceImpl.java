package org.example.eoullimback.place;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback._common.util.FileUtil;
import org.example.eoullimback.room.dto.request.SaveRoomRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService{
    private final PlaceRepository placeRepository;

    @Override
    public Place placeCreate(PlaceRequest.CreateDTO request) {
        String profileImageFileName = null;

        if (request.getProfileImage() != null) {
            try {
                if (FileUtil.isImageFile((request.getProfileImage()))) {
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
    public PlaceResponse.DetailDTO placeDetail(Long placeId) {

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new Exception404(ErrorCode.PLACE_NOT_FOUND));

        return new PlaceResponse.DetailDTO(place);
    }

    @Override
    public PlaceResponse placeUpdateView(Long placeId) {


    }

    @Override
    public PlaceResponse placeUpdate(Long placeId, SaveRoomRequestDTO request) {
        return null;
    }

    @Override
    public void placeDelete(Long placeId) {

    }
}
