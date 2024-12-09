package ru.se.info.tinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.FabricTextureDto;
import ru.se.info.tinder.dto.RequestFabricTextureDto;
import ru.se.info.tinder.model.FabricTexture;
import ru.se.info.tinder.repository.FabricTextureRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FabricTextureServiceTest {

    @Mock
    private FabricTextureRepository fabricTextureRepository;

    @InjectMocks
    private FabricTextureService fabricTextureService;

    private FabricTexture fabricTexture;
    private FabricTextureDto fabricTextureDto;
    private RequestFabricTextureDto requestFabricTextureDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fabricTexture = new FabricTexture();
        fabricTexture.setId(1L);
        fabricTexture.setName("Texture 1");

        fabricTextureDto = new FabricTextureDto();
        fabricTextureDto.setId(1L);
        fabricTextureDto.setName("Texture 1");

        requestFabricTextureDto = new RequestFabricTextureDto();
        requestFabricTextureDto.setName("Texture 1");
    }

    @Test
    void createFabricTexture_shouldReturnFabricTextureDto() {
        when(fabricTextureRepository.save(any(FabricTexture.class))).thenReturn(Mono.just(fabricTexture));

        Mono<FabricTextureDto> result = fabricTextureService.createFabricTexture(requestFabricTextureDto);

        StepVerifier.create(result)
                .expectNext(fabricTextureDto)
                .verifyComplete();

        verify(fabricTextureRepository, times(1)).save(any(FabricTexture.class));
    }

    @Test
    void getAllFabricTextures_shouldReturnFabricTextureDtoList() {
        when(fabricTextureRepository.findAll()).thenReturn(Flux.just(fabricTexture));

        Flux<FabricTextureDto> result = fabricTextureService.getAllFabricTextures();

        StepVerifier.create(result)
                .expectNext(fabricTextureDto)
                .verifyComplete();

        verify(fabricTextureRepository, times(1)).findAll();
    }

    @Test
    void getFabricTextureById_shouldReturnFabricTextureDto() {
        when(fabricTextureRepository.findById(1L)).thenReturn(Mono.just(fabricTexture));

        Mono<FabricTextureDto> result = fabricTextureService.getFabricTextureById(1L);

        StepVerifier.create(result)
                .expectNext(fabricTextureDto)
                .verifyComplete();

        verify(fabricTextureRepository, times(1)).findById(1L);
    }

    @Test
    void getFabricTextureById_shouldThrowException_whenNotFound() {
        when(fabricTextureRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<FabricTextureDto> result = fabricTextureService.getFabricTextureById(1L);

        StepVerifier.create(result)
                .expectError(NoEntityWithSuchIdException.class)
                .verify();

        verify(fabricTextureRepository, times(1)).findById(1L);
    }

    @Test
    void updateFabricTextureById_shouldReturnUpdatedFabricTextureDto() {
        when(fabricTextureRepository.findById(1L)).thenReturn(Mono.just(fabricTexture));
        when(fabricTextureRepository.save(any(FabricTexture.class))).thenReturn(Mono.just(fabricTexture));

        RequestFabricTextureDto updatedRequest = new RequestFabricTextureDto();
        updatedRequest.setName("Updated Texture");

        fabricTexture.setName("Updated Texture");

        FabricTextureDto updatedFabricTextureDto = new FabricTextureDto();
        updatedFabricTextureDto.setId(1L);
        updatedFabricTextureDto.setName("Updated Texture");

        Mono<FabricTextureDto> result = fabricTextureService.updateFabricTextureById(1L, updatedRequest);

        StepVerifier.create(result)
                .expectNext(updatedFabricTextureDto)
                .verifyComplete();

        verify(fabricTextureRepository, times(1)).findById(1L);
        verify(fabricTextureRepository, times(1)).save(any(FabricTexture.class));
    }

    @Test
    void updateFabricTextureById_shouldThrowException_whenNotFound() {
        when(fabricTextureRepository.findById(1L)).thenReturn(Mono.empty());

        RequestFabricTextureDto updatedRequest = new RequestFabricTextureDto();
        updatedRequest.setName("Updated Texture");

        Mono<FabricTextureDto> result = fabricTextureService.updateFabricTextureById(1L, updatedRequest);

        StepVerifier.create(result)
                .expectError(NoEntityWithSuchIdException.class)
                .verify();

        verify(fabricTextureRepository, times(1)).findById(1L);
    }

    @Test
    void deleteFabricTextureById_shouldDeleteSuccessfully() {
        when(fabricTextureRepository.findById(1L)).thenReturn(Mono.just(fabricTexture));
        when(fabricTextureRepository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = fabricTextureService.deleteFabricTextureById(1L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(fabricTextureRepository, times(1)).findById(1L);
        verify(fabricTextureRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteFabricTextureById_shouldThrowException_whenNotFound() {
        when(fabricTextureRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = fabricTextureService.deleteFabricTextureById(1L);

        StepVerifier.create(result)
                .expectError(NoEntityWithSuchIdException.class)
                .verify();

        verify(fabricTextureRepository, times(1)).findById(1L);
    }
}
