package org.example.eoullimback.room;

import org.example.eoullimback._common.enums.place.Category;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.place.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByStatus(RoomStatus roomStatus);

    @Query("""
        SELECT r FROM Room r JOIN FETCH r.place WHERE r.id = :roomId
    """)
    Optional<Room> findByWithPlace(@Param("roomId") Long roomId);

    List<Room> findByPlaceId(Long placeId);

    @Query(
            value = " SELECT DISTINCT r FROM Room r " +
                    " WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    " OR LOWER(r.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    " OR STR(r.place.id) LIKE :keyword" +
                    " ORDER BY r.id DESC",
            countQuery = "SELECT COUNT(DISTINCT r) FROM Room r " +
                    " WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    " OR LOWER(r.content) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
                    " OR STR(r.place.id) LIKE :keyword")
    Page<Room> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.name LIKE %:keyword% AND r.status = :status")
    Page<Room> searchByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") RoomStatus status, Pageable pageable);

    Page<Room> searchByStatus(RoomStatus status, Pageable pageable);
}
