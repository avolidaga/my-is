package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.dto.location.CreateLocationDto;
import ru.se.ifmo.tinder.dto.location.LocationDto;
import ru.se.ifmo.tinder.service.LocationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/locations")
@SecurityRequirement(name = "basicAuth")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody CreateLocationDto dto) {
        LocationDto locationDto = locationService.createLocation(dto);
        return new ResponseEntity<>(locationDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<LocationDto> updateLocation(@Valid @RequestBody LocationDto dto) {
        LocationDto locationDto = locationService.updateLocation(dto);
        return ResponseEntity.ok(locationDto);
    }

    @DeleteMapping("{locationId}")
    public ResponseEntity<Void> deleteLocationById(@NotNull @PathVariable Long locationId) {
        locationService.deleteLocationById(locationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{locationId}")
    public ResponseEntity<LocationDto> getLocationById(@NotNull @PathVariable Long locationId) {
        LocationDto locationDto = locationService.getLocationDtoById(locationId);
        return ResponseEntity.ok(locationDto);
    }

    @GetMapping
    public ResponseEntity<Page<LocationDto>> getAllLocations(@RequestParam int page,
                                                          @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LocationDto> locationDtoPage = locationService.getAllLocations(pageable);
        return ResponseEntity.ok(locationDtoPage);
    }
}
