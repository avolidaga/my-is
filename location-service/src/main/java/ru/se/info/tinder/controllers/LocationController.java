package ru.se.info.tinder.controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create a new location", description = "Creates a new location in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Location created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Mono<LocationDto> createLocation(@Valid @RequestBody RequestLocationDto dto) {
        return locationService.createLocation(dto);
    }

    @PutMapping("{locationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update location by ID", description = "Updates an existing location by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Mono<LocationDto> updateLocation(@NotNull @PathVariable Long locationId,
                                            @Valid @RequestBody RequestLocationDto dto) {
        return locationService.updateLocationById(locationId, dto);
    }

    @DeleteMapping("{locationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete location by ID", description = "Deletes an existing location by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Location deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Mono<Void> deleteLocationById(@NotNull @PathVariable Long locationId) {
        return locationService.deleteLocationById(locationId);
    }

    @GetMapping("{locationId}")
    @Operation(summary = "Get location by ID", description = "Fetches a location by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location found"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Mono<LocationDto> getLocationById(@NotNull @PathVariable Long locationId) {
        return locationService.getLocationDtoById(locationId);
    }

    @GetMapping("list")
    @Operation(summary = "Get locations by IDs", description = "Fetches locations by a list of IDs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locations found"),
            @ApiResponse(responseCode = "404", description = "Some locations not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Flux<LocationDto> getLocationsByIds(@NotNull @RequestParam List<Long> locationIds) {
        return locationService.getLocationsListByIds(Flux.fromIterable(locationIds));
    }

    @GetMapping
    @Operation(summary = "Get all locations", description = "Fetches all locations with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locations retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Flux<LocationDto> getAllLocations(@RequestParam(defaultValue = "0") @Min(0) int page,
                                             @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return locationService.getAllLocations()
                .skip((long) page * size)
                .take(size);
    }
}
