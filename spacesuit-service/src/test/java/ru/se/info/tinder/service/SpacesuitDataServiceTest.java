package ru.se.info.tinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.*;
import ru.se.info.tinder.feign.FabricTextureClient;
import ru.se.info.tinder.mapper.FabricTextureMapper;
import ru.se.info.tinder.mapper.SpacesuitDataMapper;
import ru.se.info.tinder.mapper.UserRequestMapper;
import ru.se.info.tinder.model.FabricTexture;
import ru.se.info.tinder.model.SpacesuitData;
import ru.se.info.tinder.model.User;
import ru.se.info.tinder.model.UserRequest;
import ru.se.info.tinder.model.enums.RequestStatus;
import ru.se.info.tinder.repository.SpacesuitDataRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SpacesuitDataServiceTest {

    @Mock
    private SpacesuitDataRepository spacesuitDataRepository;

    @Mock
    private UserRequestService userRequestService;

    @Mock
    private FabricTextureClient fabricTextureService;

    @InjectMocks
    private SpacesuitDataService spacesuitDataService;

    private SpacesuitData spacesuitData;
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = User.builder().username("testUser").build();

        spacesuitData = SpacesuitData.builder()
                .id(1L)
                .ownerUser(user)
                .build();

        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");
    }

    @Test
    void createSpacesuitData_shouldCreateAndReturnUserRequest() {
        // Arrange
        CreateSpacesuitDataDto createDto = CreateSpacesuitDataDto.builder().fabricTextureId(1L).build();
        FabricTextureDto fabricTextureDto = FabricTextureDto.builder().id(1L).build();
        FabricTexture fabricTexture = FabricTexture.builder().id(1L).build();
        UserRequest userRequest = UserRequest.builder().userRequestId(1L).status(RequestStatus.NEW).build();

        when(fabricTextureService.getFabricTextureById(1L, "token"))
                .thenReturn(Mono.just(fabricTextureDto)); // Mock fabricTextureService
        when(FabricTextureMapper.toEntityFabricTexture(fabricTextureDto))
                .thenReturn(fabricTexture); // Mock mapper behavior
        when(spacesuitDataRepository.save(any(SpacesuitData.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Simulate save behavior
        when(userRequestService.createUserRequest(any(SpacesuitData.class)))
                .thenReturn(Mono.just(userRequest)); // Mock createUserRequest with UserRequest

        // Act & Assert
        StepVerifier.create(spacesuitDataService.createSpacesuitData(createDto, "token"))
                .expectNextMatches(result -> result.getUserRequestId().equals(userRequest.getUserRequestId())
                        && result.getStatus() == RequestStatus.NEW)
                .verifyComplete();

        // Verify method calls
        verify(spacesuitDataRepository, times(1)).save(any(SpacesuitData.class));
        verify(userRequestService, times(1)).createUserRequest(any(SpacesuitData.class));
    }


    @Test
    void createSpacesuitData_shouldThrowException_whenFabricTextureNotFound() {
        CreateSpacesuitDataDto createDto = CreateSpacesuitDataDto.builder().fabricTextureId(1L).build();

        when(fabricTextureService.getFabricTextureById(1L, "token"))
                .thenReturn(Mono.empty());

        StepVerifier.create(spacesuitDataService.createSpacesuitData(createDto, "token"))
                .expectError(NoEntityWithSuchIdException.class)
                .verify();

        verify(spacesuitDataRepository, never()).save(any(SpacesuitData.class));
    }

    @Test
    void getCurrentUserSpacesuitData_shouldReturnSpacesuitDataForUser() {
        List<SpacesuitData> spacesuitDataList = List.of(spacesuitData);

        when(spacesuitDataRepository.findAllUserSpacesuitDataByOwnerUserUsername("testUser"))
                .thenReturn(spacesuitDataList);

        StepVerifier.create(spacesuitDataService.getCurrentUserSpacesuitData(principal))
                .expectNextMatches(dto -> dto.getId().equals(1L))
                .verifyComplete();

        verify(spacesuitDataRepository, times(1))
                .findAllUserSpacesuitDataByOwnerUserUsername("testUser");
    }

    @Test
    void deleteSpacesuitData_shouldDeleteSpacesuitData() {
        when(spacesuitDataRepository.findById(1L))
                .thenReturn(Optional.of(spacesuitData));

        StepVerifier.create(spacesuitDataService.deleteSpacesuitData(1L, principal))
                .verifyComplete();

        verify(spacesuitDataRepository, times(1)).delete(spacesuitData);
    }

    @Test
    void deleteSpacesuitData_shouldThrowException_whenUserNotAuthorized() {
        spacesuitData.getOwnerUser().setUsername("otherUser");

        when(spacesuitDataRepository.findById(1L))
                .thenReturn(Optional.of(spacesuitData));

        StepVerifier.create(spacesuitDataService.deleteSpacesuitData(1L, principal))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(spacesuitDataRepository, never()).delete(spacesuitData);
    }

    @Test
    void updateSpacesuitData_shouldUpdateAndReturnSpacesuitData() {
        UpdateSpacesuitDataDto updateDto = UpdateSpacesuitDataDto.builder().id(1L).fabricTextureId(1L).build();
        FabricTextureDto fabricTextureDto = FabricTextureDto.builder().id(1L).build();

        when(spacesuitDataRepository.findById(1L))
                .thenReturn(Optional.of(spacesuitData));
        when(fabricTextureService.getFabricTextureById(1L, "token"))
                .thenReturn(Mono.just(fabricTextureDto));
        when(spacesuitDataRepository.save(any(SpacesuitData.class)))
                .thenReturn(spacesuitData);

        StepVerifier.create(spacesuitDataService.updateSpacesuitData(updateDto, principal, "token"))
                .expectNextMatches(dto -> dto.getId().equals(1L))
                .verifyComplete();

        verify(spacesuitDataRepository, times(1)).save(any(SpacesuitData.class));
    }

    @Test
    void getSpacesuitData_shouldReturnSpacesuitData() {
        when(spacesuitDataRepository.findById(1L))
                .thenReturn(Optional.of(spacesuitData));

        StepVerifier.create(spacesuitDataService.getSpacesuitData(1L, principal))
                .expectNextMatches(dto -> dto.getId().equals(1L))
                .verifyComplete();

        verify(spacesuitDataRepository, times(1)).findById(1L);
    }

    @Test
    void getSpacesuitData_shouldThrowException_whenUserNotAuthorized() {
        spacesuitData.getOwnerUser().setUsername("otherUser");

        when(spacesuitDataRepository.findById(1L))
                .thenReturn(Optional.of(spacesuitData));

        StepVerifier.create(spacesuitDataService.getSpacesuitData(1L, principal))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
