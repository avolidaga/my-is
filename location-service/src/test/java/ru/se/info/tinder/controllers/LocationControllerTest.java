package ru.se.info.tinder.controllers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.LocationDto;
import ru.se.info.tinder.dto.RequestLocationDto;
import ru.se.info.tinder.repository.LocationRepository;
import ru.se.info.tinder.service.LocationService;

import java.util.List;

import static org.mockito.Mockito.*;

class LocationControllerTest {
    @MockBean
    private LocationRepository locationRepository;

    private final LocationService locationService = mock(LocationService.class);
    private final LocationController controller = new LocationController(locationService);

    @Test
    void getAllLocations_shouldReturnListOfLocations() {
        LocationDto locationDto = new LocationDto(1L, "Location 1", "Description", 25.0);
        List<LocationDto> locations = List.of(locationDto);

        when(locationService.getAllLocations()).thenReturn(Flux.fromIterable(locations));

        StepVerifier.create(controller.getAllLocations(0, 10))
                .expectNextMatches(location -> location.getName().equals("Location 1") && location.getTemperature() == 25.0)
                .verifyComplete();
    }

    @Test
    void createLocation_shouldCreateNewLocation() {
        RequestLocationDto requestLocationDto = new RequestLocationDto("New Location", "New location description", 22.0);
        LocationDto locationDto = new LocationDto(1L, "New Location", "New location description", 22.0);

        when(locationService.createLocation(requestLocationDto)).thenReturn(Mono.just(locationDto));

        StepVerifier.create(controller.createLocation(requestLocationDto))
                .expectNextMatches(location -> location.getName().equals("New Location") && location.getTemperature() == 22.0)
                .verifyComplete();
    }

    @Test
    void updateLocation_shouldUpdateLocation() {
        Long locationId = 1L;
        RequestLocationDto requestLocationDto = new RequestLocationDto("Updated Location", "Updated description", 26.0);
        LocationDto locationDto = new LocationDto(1L, "Updated Location", "Updated description", 26.0);

        when(locationService.updateLocationById(locationId, requestLocationDto)).thenReturn(Mono.just(locationDto));

        StepVerifier.create(controller.updateLocation(locationId, requestLocationDto))
                .expectNextMatches(location -> location.getName().equals("Updated Location") && location.getTemperature() == 26.0)
                .verifyComplete();
    }

    @Test
    void deleteLocation_shouldDeleteLocation() {
        Long locationId = 1L;

        when(locationService.deleteLocationById(locationId)).thenReturn(Mono.empty());

        StepVerifier.create(controller.deleteLocationById(locationId))
                .verifyComplete();
    }

    @Test
    void getLocationById_shouldReturnLocationById() {
        Long locationId = 1L;
        LocationDto locationDto = new LocationDto(1L, "Location 1", "Description", 25.0);

        when(locationService.getLocationDtoById(locationId)).thenReturn(Mono.just(locationDto));

        StepVerifier.create(controller.getLocationById(locationId))
                .expectNextMatches(location -> location.getName().equals("Location 1") && location.getTemperature() == 25.0)
                .verifyComplete();
    }

    @Test
    @Disabled
    void getLocationsByIds_shouldReturnLocationsByIds() {
        List<Long> locationIds = List.of(1L, 2L);
        LocationDto locationDto1 = new LocationDto(1L, "Location 1", "Description", 25.0);
        LocationDto locationDto2 = new LocationDto(2L, "Location 2", "Description 2", 30.0);
        List<LocationDto> locations = List.of(locationDto1, locationDto2);

        // Мокируем возврат Flux из сервиса
        when(locationService.getLocationsListByIds(Flux.fromIterable(locationIds)))
                .thenReturn(Flux.fromIterable(locations));

        // Проверяем, что контроллер вернет правильные локации
        StepVerifier.create(controller.getLocationsByIds(locationIds))
                .expectNextMatches(location -> location.getName().equals("Location 1") && location.getTemperature() == 25.0)
                .expectNextMatches(location -> location.getName().equals("Location 2") && location.getTemperature() == 30.0)
                .verifyComplete();
    }

}
