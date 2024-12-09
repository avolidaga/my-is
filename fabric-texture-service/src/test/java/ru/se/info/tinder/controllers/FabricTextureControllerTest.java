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
        // Инициализация моков
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFabricTexture_shouldCreateNewFabricTexture() {
        // Мокаем входные и выходные данные
        RequestFabricTextureDto requestFabricTextureDto = new RequestFabricTextureDto("New Texture", "Description of new texture");
        FabricTextureDto fabricTextureDto = new FabricTextureDto(1L, "New Texture", "Description of new texture");

        // Мокаем сервис
        when(fabricTextureService.createFabricTexture(requestFabricTextureDto)).thenReturn(Mono.just(fabricTextureDto));

        // Проверяем, что контроллер возвращает правильный результат
        FabricTextureDto result = controller.createFabricTexture(requestFabricTextureDto).block();

        // Проверяем данные, которые возвращает контроллер
        assertNotNull(result);
        assertEquals("New Texture", result.getName());
        assertEquals("Description of new texture", result.getDescription());

        // Проверяем, что сервис был вызван
        verify(fabricTextureService, times(1)).createFabricTexture(requestFabricTextureDto);
    }

    @Test
    void updateFabricTexture_shouldUpdateFabricTexture() {
        Long fabricTextureId = 1L;
        RequestFabricTextureDto requestFabricTextureDto = new RequestFabricTextureDto("Updated Texture", "Updated description");
        FabricTextureDto fabricTextureDto = new FabricTextureDto(1L, "Updated Texture", "Updated description");

        // Мокаем сервис, возвращаем Mono.just(fabricTextureDto), так как метод возвращает Mono
        when(fabricTextureService.updateFabricTextureById(fabricTextureId, requestFabricTextureDto))
                .thenReturn(Mono.just(fabricTextureDto));

        // Проверяем, что контроллер возвращает правильный результат
        FabricTextureDto result = controller.updateFabricTexture(fabricTextureId, requestFabricTextureDto).block();

        // Проверяем данные
        assertNotNull(result);
        assertEquals("Updated Texture", result.getName());
        assertEquals("Updated description", result.getDescription());

        // Проверяем, что сервис был вызван
        verify(fabricTextureService, times(1)).updateFabricTextureById(fabricTextureId, requestFabricTextureDto);
    }


    @Test
    void getFabricTextureById_shouldReturnFabricTextureById() {
        Long fabricTextureId = 1L;
        FabricTextureDto fabricTextureDto = new FabricTextureDto(1L, "Texture 1", "Description");

        // Мокаем сервис, правильно возвращаем Mono.just(fabricTextureDto)
        when(fabricTextureService.getFabricTextureById(fabricTextureId)).thenReturn(Mono.just(fabricTextureDto));

        // Проверяем, что контроллер возвращает правильный результат
        FabricTextureDto result = controller.getFabricTextureById(fabricTextureId).block(); // block() для Mono

        // Проверяем данные
        assertNotNull(result);
        assertEquals("Texture 1", result.getName());
        assertEquals("Description", result.getDescription());

        // Проверяем, что сервис был вызван
        verify(fabricTextureService, times(1)).getFabricTextureById(fabricTextureId);
    }


    @Test
    void deleteFabricTextureById_shouldDeleteFabricTexture() {
        Long fabricTextureId = 1L;

        // Мокаем успешное удаление, возвращаем Mono.empty(), так как метод возвращает Mono<Void>
        when(fabricTextureService.deleteFabricTextureById(fabricTextureId)).thenReturn(Mono.empty());

        // Проверяем контроллер
        controller.deleteFabricTextureById(fabricTextureId);

        // Проверяем, что сервис был вызван
        verify(fabricTextureService, times(1)).deleteFabricTextureById(fabricTextureId);

        // Проверка, что не было лишних вызовов
        verifyNoMoreInteractions(fabricTextureService);
    }


    @Test
    void getAllFabricTextures_shouldReturnListOfFabricTextures() {
        FabricTextureDto fabricTextureDto = new FabricTextureDto(1L, "Texture 1", "Description");
        List<FabricTextureDto> fabricTextures = List.of(fabricTextureDto);

        // Мокаем возврат данных из сервиса, возвращаем Flux.fromIterable() для возвращения потока данных
        when(fabricTextureService.getAllFabricTextures()).thenReturn(Flux.fromIterable(fabricTextures));

        // Проверяем, что контроллер возвращает правильный результат
        Flux<FabricTextureDto> result = controller.getAllFabricTextures(0, 10);

        // Проверяем результаты
        StepVerifier.create(result)
                .expectNextMatches(fabricTexture -> fabricTexture.getName().equals("Texture 1") && fabricTexture.getDescription().equals("Description"))
                .verifyComplete();

        // Проверяем, что сервис был вызван
        verify(fabricTextureService, times(1)).getAllFabricTextures();
    }
}
