package org.example.eoullimback.place.dto.response;

import org.example.eoullimback._common.enums.place.Category;
import org.example.eoullimback.place.Place;

public record SavePlaceResponseDTO(
        String name,
        String address,
        Category category
) {
    public static SavePlaceResponseDTO from(Place place) {
        return new SavePlaceResponseDTO(
                place.getName(),
                place.getAddress(),
                place.getCategory()
        );
    }
}
