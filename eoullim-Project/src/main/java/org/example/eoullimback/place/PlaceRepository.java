package org.example.eoullimback.place;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query(
            value = " SELECT DISTINCT p FROM Place p " +
                    " WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    " OR LOWER(p.address) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    " OR LOWER(p.category) LIKE LOWER(:keyword) " +
                    " ORDER BY p.createdAt DESC",
            countQuery = "SELECT COUNT(DISTINCT p) FROM Place p " +
                         " WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                         " OR LOWER(p.address) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
                         " OR LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Place> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(
            value = "SELECT * FROM places ORDER BY created_at DESC LIMIT 4",
            nativeQuery = true
    )
    List<Place> findLatest4Places();

}
