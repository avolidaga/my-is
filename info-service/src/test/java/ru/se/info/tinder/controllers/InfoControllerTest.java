package ru.se.info.tinder.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.InfoDto;
import ru.se.info.tinder.dto.RequestInfoDto;
import ru.se.info.tinder.repository.InfoRepository;
import ru.se.info.tinder.service.InfoService;

import java.util.List;

import static org.mockito.Mockito.*;

class InfoControllerTest {
    @MockBean
    private InfoRepository infoRepository;

    private final InfoService infoService = mock(InfoService.class);
    private final InfoController controller = new InfoController(infoService);

    @Test
    void getAllLocations_shouldReturnListOfLocations() {
        InfoDto infoDto = new InfoDto(1L, "Location 1", "Description", 25.0);
        List<InfoDto> locations = List.of(infoDto);

        when(infoService.getAllLocations()).thenReturn(Flux.fromIterable(locations));

        StepVerifier.create(controller.getAllLocations(0, 10))
                .expectNextMatches(location -> location.getName().equals("Location 1") && location.getTemperature() == 25.0)
                .verifyComplete();
    }

    @Test
    void createLocation_shouldCreateNewLocation() {
        RequestInfoDto requestInfoDto = new RequestInfoDto("New Location", "New location description", 22.0);
        InfoDto infoDto = new InfoDto(1L, "New Location", "New location description", 22.0);

        when(infoService.createLocation(requestInfoDto)).thenReturn(Mono.just(infoDto));

        StepVerifier.create(controller.createLocation(requestInfoDto))
                .expectNextMatches(location -> location.getName().equals("New Location") && location.getTemperature() == 22.0)
                .verifyComplete();
    }

    @Test
    void updateLocation_shouldUpdateLocation() {
        Long locationId = 1L;
        RequestInfoDto requestInfoDto = new RequestInfoDto("Updated Location", "Updated description", 26.0);
        InfoDto infoDto = new InfoDto(1L, "Updated Location", "Updated description", 26.0);

        when(infoService.updateLocationById(locationId, requestInfoDto)).thenReturn(Mono.just(infoDto));

        StepVerifier.create(controller.updateLocation(locationId, requestInfoDto))
                .expectNextMatches(location -> location.getName().equals("Updated Location") && location.getTemperature() == 26.0)
                .verifyComplete();
    }

    @Test
    void deleteLocation_shouldDeleteLocation() {
        Long locationId = 1L;

        when(infoService.deleteLocationById(locationId)).thenReturn(Mono.empty());

        StepVerifier.create(controller.deleteLocationById(locationId))
                .verifyComplete();
    }

    @Test
    void getLocationById_shouldReturnLocationById() {
        Long locationId = 1L;
        InfoDto infoDto = new InfoDto(1L, "Location 1", "Description", 25.0);

        when(infoService.getLocationDtoById(locationId)).thenReturn(Mono.just(infoDto));

        StepVerifier.create(controller.getLocationById(locationId))
                .expectNextMatches(location -> location.getName().equals("Location 1") && location.getTemperature() == 25.0)
                .verifyComplete();
    }

    @Test
    void getLocationsByIds_shouldReturnLocationsByIds() {
        List<Long> locationIds = List.of(1L, 2L);
        InfoDto infoDto1 = new InfoDto(1L, "Location 1", "Description", 25.0);
        InfoDto infoDto2 = new InfoDto(2L, "Location 2", "Description 2", 30.0);
        List<InfoDto> locations = List.of(infoDto1, infoDto2);

        when(infoService.getLocationsListByIds(Flux.fromIterable(locationIds)))
                .thenReturn(Flux.fromIterable(locations));

        StepVerifier.create(controller.getLocationsByIds(locationIds))
                .expectNextMatches(location -> location.getName().equals("Location 1") && location.getTemperature() == 25.0)
                .expectNextMatches(location -> location.getName().equals("Location 2") && location.getTemperature() == 30.0);
    }
}
