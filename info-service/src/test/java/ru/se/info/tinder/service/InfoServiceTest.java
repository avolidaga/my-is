package ru.se.info.tinder.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.InfoDto;
import ru.se.info.tinder.dto.RequestInfoDto;
import ru.se.info.tinder.model.Info;
import ru.se.info.tinder.repository.InfoRepository;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InfoServiceTest {

    @Mock
    private InfoRepository infoRepository;

    @InjectMocks
    private InfoService infoService;

    @Test
    public void testCreateLocation() {
        RequestInfoDto request = new RequestInfoDto("New City", "New description", 30.0);
        Info savedInfo = new Info(1L, "New City", "New description", 30.0);
        InfoDto expectedDto = new InfoDto(1L, "New City", "New description", 30.0);

        when(infoRepository.save(any(Info.class))).thenReturn(Mono.just(savedInfo));

        Mono<InfoDto> result = infoService.createLocation(request);

        StepVerifier.create(result)
                .expectNext(expectedDto)
                .verifyComplete();
    }

    @Test
    public void testUpdateLocationById_Found() {
        Long locationId = 1L;
        RequestInfoDto request = new RequestInfoDto("Updated City", "Updated description", 32.0);
        Info existingInfo = new Info(locationId, "Old City", "Old description", 30.0);
        Info updatedInfo = new Info(locationId, "Updated City", "Updated description", 32.0);
        InfoDto expectedDto = new InfoDto(locationId, "Updated City", "Updated description", 32.0);

        when(infoRepository.findById(locationId)).thenReturn(Mono.just(existingInfo));
        when(infoRepository.save(any(Info.class))).thenReturn(Mono.just(updatedInfo));

        Mono<InfoDto> result = infoService.updateLocationById(locationId, request);

        StepVerifier.create(result)
                .expectNext(expectedDto)
                .verifyComplete();
    }

    @Test
    public void testGetAllLocations() {
        Info info1 = new Info(1L, "City1", "Description1", 30.0);
        Info info2 = new Info(2L, "City2", "Description2", 25.0);
        InfoDto infoDto1 = new InfoDto(1L, "City1", "Description1", 30.0);
        InfoDto infoDto2 = new InfoDto(2L, "City2", "Description2", 25.0);

        when(infoRepository.findAll()).thenReturn(Flux.just(info1, info2));

        Flux<InfoDto> result = infoService.getAllLocations();

        StepVerifier.create(result)
                .expectNext(infoDto1)
                .expectNext(infoDto2)
                .verifyComplete();
    }

    @Test
    public void testDeleteLocationById() {
        Long locationId = 1L;

        when(infoRepository.deleteById(locationId)).thenReturn(Mono.empty());

        Mono<Void> result = infoService.deleteLocationById(locationId);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    public void testGetLocationDtoById_Found() {
        Long locationId = 1L;
        Info info = new Info(locationId, "City", "Description", 30.0);
        InfoDto expectedDto = new InfoDto(locationId, "City", "Description", 30.0);

        when(infoRepository.findById(locationId)).thenReturn(Mono.just(info));

        Mono<InfoDto> result = infoService.getLocationDtoById(locationId);

        StepVerifier.create(result)
                .expectNext(expectedDto)
                .verifyComplete();
    }
}
