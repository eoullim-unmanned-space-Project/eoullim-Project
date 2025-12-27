package org.example.eoullimback.room;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    void deleteByPlaceId(Long placeId);
}
