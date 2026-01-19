package org.example.eoullimback.place;

import lombok.Data;
import org.example.eoullimback._common.enums.place.Category;
import org.example.eoullimback._common.util.DateTimeUtil;

import java.math.BigDecimal;

public class PlaceResponse {

    @Data
    public static class ListDTO {
        private Long id;
        private String name;
        private String address;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private Category category;
        private String profilePath;

        public ListDTO(Place place) {
            this.id = place.getId();
            this.name = place.getName();
            this.address = place.getAddress();
            this.latitude = place.getLatitude();
            this.longitude = place.getLongitude();
            this.category = place.getCategory();
            this.profilePath = "/images/" + place.getProfileImage();
        }
    }

    @Data
    public static class DetailDTO {
        private Long id;
        private String name;
        private String address;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private Category category;
        private String createdAt;
        private String updatedAt;
        private String profilePath;

        public DetailDTO(Place place) {
            this.id = place.getId();
            this.name = place.getName();
            this.address = place.getAddress();
            this.latitude = place.getLatitude();
            this.longitude = place.getLongitude();
            this.category = place.getCategory();
            this.createdAt = DateTimeUtil.toKstString(place.getCreatedAt());
            this.updatedAt = DateTimeUtil.toKstString(place.getUpdatedAt());
            this.profilePath = "/images/" + place.getProfileImage();
        }
    }
}
