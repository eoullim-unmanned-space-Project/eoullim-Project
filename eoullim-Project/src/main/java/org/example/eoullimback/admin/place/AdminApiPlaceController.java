package org.example.eoullimback.admin.place;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.place.Place;
import org.example.eoullimback.place.PlaceRequest;
import org.example.eoullimback.place.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminApiPlaceController {

    private final PlaceService placeService;

    @PostMapping("/api/admin/places")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Place> createProc(PlaceRequest.CreateDTO request) {

        request.validate();
        Place place = placeService.placeCreate(request);

        return ResponseEntity.ok().body(place);
    }

    @PutMapping("/api/admin/places/{placeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Place>> UpdateProc(@PathVariable Long placeId,
                                                         PlaceRequest.UpdateDTO request
    ) {
        request.validate();

        Place place = placeService.placeUpdate(placeId, request);

        return ResponseEntity.ok().body(Map.of("place", place));
    }

    @DeleteMapping("/api/admin/places/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProc(@PathVariable(name = "id") Long placeId) {

        placeService.placeDelete(placeId);

        return ResponseEntity.ok().body(null);

    }
}
