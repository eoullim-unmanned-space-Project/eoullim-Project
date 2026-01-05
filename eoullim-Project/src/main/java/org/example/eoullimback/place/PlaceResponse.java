package org.example.eoullimback.place;

import lombok.Data;
import org.example.eoullimback._common.enums.place.Category;
import org.example.eoullimback._common.util.DateTimeUtil;

import java.math.BigDecimal;

public class PlaceResponse {

    @Data
    public static class ListDTO {
        private Long Id;
        private String name;
        private String address;
        private Category category;
        private String profilePath;

        public ListDTO(Place place) {
            this.Id = place.getId();
            this.name = place.getName();
            this.address = place.getAddress();
            this.category = place.getCategory();
            this.profilePath = "/images/" + place.getProfileImage();
        }
    }

    @Data
    public static class DetailDTO {
        private String name;
        private String address;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private Category category;
        private String createAt;
        private String profilePath;

        public DetailDTO(Place place) {
            this.name = place.getName();
            this.address = place.getAddress();
            this.latitude = place.getLatitude();
            this.longitude = place.getLongitude();
            this.category = place.getCategory();
            this.createAt = DateTimeUtil.toKstString(place.getCreatedAt());
            this.profilePath = "/images/" + place.getProfileImage();
        }
    }
}
