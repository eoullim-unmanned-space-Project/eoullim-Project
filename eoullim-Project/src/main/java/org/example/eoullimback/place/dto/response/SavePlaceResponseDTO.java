package org.example.eoullimback.place.dto.response;

import org.example.eoullimback._common.enums.place.PlaceStatus;
import org.example.eoullimback.place.Place;

public record SavePlaceResponseDTO(
        String name,
        String address,
        PlaceStatus status
) {
    public static SavePlaceResponseDTO from(Place place) {
        return new SavePlaceResponseDTO(
                place.getName(),
                place.getAddress(),
                place.getStatus()
        );
    }
}
