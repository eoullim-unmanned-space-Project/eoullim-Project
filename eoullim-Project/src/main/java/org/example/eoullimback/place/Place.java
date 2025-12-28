package org.example.eoullimback.place;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.place.Category;

import java.math.BigDecimal;

@Entity
@Table(name = "places")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private String profileImage;

    @Builder
    public Place(Long id, String name, String address, BigDecimal latitude, BigDecimal longitude, Category category, String profileImage) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.profileImage = profileImage;
    }

    public void update(PlaceRequest.UpdateDTO request) {
        this.name = request.getName();
        this.category = request.getCategory();
        this.profileImage = request.getProfileImageFileName();
    }

    public String getProfilePath() {
        if (this.profileImage == null) {
            return null;
        }
        if (this.profileImage.startsWith("http")) {
            return this.profileImage;
        }

        return "/images/" + this.profileImage;
    }

}
