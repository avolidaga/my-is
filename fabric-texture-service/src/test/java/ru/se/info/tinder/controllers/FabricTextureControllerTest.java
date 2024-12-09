package ru.se.info.tinder.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.FabricTextureDto;
import ru.se.info.tinder.dto.RequestFabricTextureDto;
import ru.se.info.tinder.service.FabricTextureService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FabricTextureControllerTest {

    @Mock
    private FabricTextureService fabricTextureService;

    @InjectMocks
    private FabricTextureController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFabricTexture_shouldCreateNewFabricTexture() {
        RequestFabricTextureDto requestFabricTextureDto = new RequestFabricTextureDto("New Texture", "Description of new texture");
        FabricTextureDto fabricTextureDto = new FabricTextureDto(1L, "New Texture", "Description of new texture");

        when(fabricTextureService.createFabricTexture(requestFabricTextureDto)).thenReturn(Mono.just(fabricTextureDto));

        FabricTextureDto result = controller.createFabricTexture(requestFabricTextureDto).block();

        assertNotNull(result);
        assertEquals("New Texture", result.getName());
        assertEquals("Description of new texture", result.getDescription());

        verify(fabricTextureService, times(1)).createFabricTexture(requestFabricTextureDto);
    }

    @Test
    void updateFabricTexture_shouldUpdateFabricTexture() {
        Long fabricTextureId = 1L;
        RequestFabricTextureDto requestFabricTextureDto = new RequestFabricTextureDto("Updated Texture", "Updated description");
        FabricTextureDto fabricTextureDto = new FabricTextureDto(1L, "Updated Texture", "Updated description");

        when(fabricTextureService.updateFabricTextureById(fabricTextureId, requestFabricTextureDto))
                .thenReturn(Mono.just(fabricTextureDto));

        FabricTextureDto result = controller.updateFabricTexture(fabricTextureId, requestFabricTextureDto).block();

        assertNotNull(result);
        assertEquals("Updated Texture", result.getName());
        assertEquals("Updated description", result.getDescription());

        verify(fabricTextureService, times(1)).updateFabricTextureById(fabricTextureId, requestFabricTextureDto);
    }

    @Test
    void getFabricTextureById_shouldReturnFabricTextureById() {
        Long fabricTextureId = 1L;
        FabricTextureDto fabricTextureDto = new FabricTextureDto(1L, "Texture 1", "Description");

        when(fabricTextureService.getFabricTextureById(fabricTextureId)).thenReturn(Mono.just(fabricTextureDto));

        FabricTextureDto result = controller.getFabricTextureById(fabricTextureId).block();

        assertNotNull(result);
        assertEquals("Texture 1", result.getName());
        assertEquals("Description", result.getDescription());

        verify(fabricTextureService, times(1)).getFabricTextureById(fabricTextureId);
    }

    @Test
    void deleteFabricTextureById_shouldDeleteFabricTexture() {
        Long fabricTextureId = 1L;

        when(fabricTextureService.deleteFabricTextureById(fabricTextureId)).thenReturn(Mono.empty());

        controller.deleteFabricTextureById(fabricTextureId);

        verify(fabricTextureService, times(1)).deleteFabricTextureById(fabricTextureId);

        verifyNoMoreInteractions(fabricTextureService);
    }

    @Test
    void getAllFabricTextures_shouldReturnListOfFabricTextures() {
        FabricTextureDto fabricTextureDto = new FabricTextureDto(1L, "Texture 1", "Description");
        List<FabricTextureDto> fabricTextures = List.of(fabricTextureDto);

        when(fabricTextureService.getAllFabricTextures()).thenReturn(Flux.fromIterable(fabricTextures));

        Flux<FabricTextureDto> result = controller.getAllFabricTextures(0, 10);

        StepVerifier.create(result)
                .expectNextMatches(fabricTexture -> fabricTexture.getName().equals("Texture 1") && fabricTexture.getDescription().equals("Description"))
                .verifyComplete();

        verify(fabricTextureService, times(1)).getAllFabricTextures();
    }
}
