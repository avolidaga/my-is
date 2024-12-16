package ru.se.info.tinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.UserRequestDto;
import ru.se.info.tinder.mapper.UserRequestMapper;
import ru.se.info.tinder.model.FabricTexture;
import ru.se.info.tinder.model.SpacesuitData;
import ru.se.info.tinder.model.UserRequest;
import ru.se.info.tinder.model.enums.RequestStatus;
import ru.se.info.tinder.model.enums.SearchStatus;
import ru.se.info.tinder.model.enums.UpdateRequestStatus;
import ru.se.info.tinder.repository.UserRequestRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserRequestServiceTest {

    @Mock
    private UserRequestRepository userRequestRepository;

    @InjectMocks
    private UserRequestService userRequestService;

    private UserRequest userRequest;
    private UserRequestDto userRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        FabricTexture fabricTexture = FabricTexture.builder()
                .id(1L)
                .build();

        SpacesuitData spacesuitData = SpacesuitData.builder()
                .id(1L)
                .head(1)
                .chest(1)
                .waist(1)
                .hips(1)
                .footSize(1)
                .height(1)
                .fabricTexture(fabricTexture) // Добавляем fabricTexture
                .build();

        userRequest = UserRequest.builder()
                .userRequestId(1L)
                .spacesuitData(spacesuitData)
                .status(RequestStatus.NEW)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRequestDto = UserRequestMapper.toUserRequestDto(userRequest);
    }


    @Test
    void getUsersRequestsByStatus_shouldReturnRequestsForStatusAll() {
        List<UserRequest> userRequests = List.of(userRequest);

        when(userRequestRepository.findAll()).thenReturn(userRequests);

        Flux<UserRequestDto> result = userRequestService.getUsersRequestsByStatus(SearchStatus.ALL);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getUserRequestId().equals(1L) && dto.getStatus() == RequestStatus.NEW)
                .verifyComplete();

        verify(userRequestRepository, times(1)).findAll();
    }


    @Test
    void getUsersRequestsByStatus_shouldReturnRequests_whenStatusIsNew() {
        List<UserRequest> userRequests = List.of(userRequest);
        when(userRequestRepository.findNew()).thenReturn(userRequests); // Исправлено: возвращаем список напрямую

        StepVerifier.create(userRequestService.getUsersRequestsByStatus(SearchStatus.NEW))
                .expectNext(userRequestDto)
                .verifyComplete();

        verify(userRequestRepository, times(1)).findNew();
    }

    @Test
    void updateUserRequestStatus_shouldUpdateStatusToInProgress() {
        when(userRequestRepository.findById(1L)).thenReturn(Optional.of(userRequest));
        when(userRequestRepository.save(any(UserRequest.class))).thenReturn(userRequest);

        StepVerifier.create(userRequestService.updateUserRequestStatus(1L, UpdateRequestStatus.IN_PROGRESS))
                .expectNextMatches(result -> result.getStatus() == RequestStatus.IN_PROGRESS)
                .verifyComplete();

        verify(userRequestRepository, times(1)).findById(1L);
        verify(userRequestRepository, times(1)).save(any(UserRequest.class));
    }

    @Test
    void updateUserRequestStatus_shouldThrowException_whenRequestNotFound() {
        when(userRequestRepository.findById(1L)).thenReturn(Optional.empty());

        StepVerifier.create(userRequestService.updateUserRequestStatus(1L, UpdateRequestStatus.IN_PROGRESS))
                .expectError(NoEntityWithSuchIdException.class)
                .verify();

        verify(userRequestRepository, times(1)).findById(1L);
    }

    @Test
    void updateUserRequestStatus_shouldThrowException_whenIncorrectStatus() {
        userRequest.setStatus(RequestStatus.READY);

        when(userRequestRepository.findById(1L)).thenReturn(Optional.of(userRequest));

        StepVerifier.create(userRequestService.updateUserRequestStatus(1L, UpdateRequestStatus.IN_PROGRESS))
                .expectError(IllegalStateException.class)
                .verify();

        verify(userRequestRepository, times(1)).findById(1L);
    }

    @Test
    void createUserRequest_shouldCreateNewRequest() {
        SpacesuitData spacesuitData = SpacesuitData.builder().id(1L).build();
        when(userRequestRepository.save(any(UserRequest.class))).thenReturn(userRequest);

        StepVerifier.create(userRequestService.createUserRequest(spacesuitData))
                .expectNextMatches(result -> result.getStatus() == RequestStatus.NEW)
                .verifyComplete();

        verify(userRequestRepository, times(1)).save(any(UserRequest.class));
    }
}
