package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.LocationDto;
import ru.se.info.tinder.dto.RequestLocationDto;
import ru.se.info.tinder.service.LocationService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    @PostMapping("new")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<LocationDto> createLocation(@Valid @RequestBody RequestLocationDto dto) {
        return locationService.createLocation(dto);
    }

    @PutMapping("{locationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<LocationDto> updateLocation(@NotNull @PathVariable Long locationId,
                                            @Valid @RequestBody RequestLocationDto dto) {
        return locationService.updateLocationById(locationId, dto);
    }

    @DeleteMapping("{locationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<Void> deleteLocationById(@NotNull @PathVariable Long locationId) {
        return locationService.deleteLocationById(locationId);
    }

    @GetMapping("{locationId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<LocationDto> getLocationById(@NotNull @PathVariable Long locationId) {
        return locationService.getLocationDtoById(locationId);
    }

    @GetMapping("list")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Flux<LocationDto> getLocationsByIds(@NotNull @RequestParam List<Long> locationIds) {
        return locationService.getLocationsListByIds(Flux.fromIterable(locationIds));
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Flux<LocationDto> getAllLocations(@RequestParam(defaultValue = "0") @Min(0) int page,
                                             @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return locationService.getAllLocations()
                .skip((long) page * size)
                .take(size);
    }
}
