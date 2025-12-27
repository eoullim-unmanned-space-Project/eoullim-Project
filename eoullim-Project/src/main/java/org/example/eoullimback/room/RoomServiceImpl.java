package org.example.eoullimback.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService{
    @Override
    public List<RoomResponse.ListDTO> roomList(Long placeId) {
        return List.of();
    }
}
