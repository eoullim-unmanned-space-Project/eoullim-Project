package org.example.eoullimback.place;

import org.example.eoullimback._common.enums.place.Category;
import org.example.eoullimback._common.util.DateTimeUtil;

import java.math.BigDecimal;

public class PlaceResponse {

    public static class ListDTO {
        private String name;
        private String address;
        private Category category;

        public ListDTO(Place place) {
            this.name = place.getName();
            this.address = place.getAddress();
            this.category = place.getCategory();
        }
    }

    public static class DetailDTO {
        private String name;
        private String address;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private Category category;
        private String createAt;

        public DetailDTO(Place place) {
            this.name = place.getName();
            this.address = place.getAddress();
            this.latitude = place.getLatitude();
            this.longitude = place.getLongitude();
            this.category = place.getCategory();
            this.createAt = DateTimeUtil.toKstString(place.getCreatedAt());
        }
    }
}
