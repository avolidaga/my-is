package ru.se.info.tinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.*;
import ru.se.info.tinder.feign.UserServiceClient;
import ru.se.info.tinder.model.FabricTexture;
import ru.se.info.tinder.model.Meeting;
import ru.se.info.tinder.model.User;
import ru.se.info.tinder.model.UserRequest;
import ru.se.info.tinder.repository.MeetingRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private UserRequestService userRequestService;

    @Mock
    private UserServiceClient fabricTextureService;

    @InjectMocks
    private MeetingService meetingService;

    private Meeting meeting;
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = User.builder().username("testUser").build();

        FabricTexture fabricTexture = FabricTexture.builder().id(1L).build(); // Mock the FabricTexture

        meeting = Meeting.builder()
                .id(1L)
                .ownerUser(user)
                .fabricTexture(fabricTexture) // Initialize fabricTexture
                .build();

        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");
    }

    @Test
    void createSpacesuitData_shouldThrowException_whenFabricTextureNotFound() {
        CreateMeetingDto createDto = CreateMeetingDto.builder().fabricTextureId(1L).build();

        when(fabricTextureService.getFabricTextureById(1L, "token"))
                .thenReturn(Mono.empty()); // Simulate FabricTexture not found

        StepVerifier.create(meetingService.createSpacesuitData(createDto, "token"))
                .expectError(NoEntityWithSuchIdException.class) // Expect exception when not found
                .verify();

        verify(meetingRepository, never()).save(any(Meeting.class)); // Ensure save is never called
    }

    @Test
    void getCurrentUserSpacesuitData_shouldReturnSpacesuitDataForUser() {
        List<Meeting> meetingList = List.of(meeting);

        when(meetingRepository.findAllMeetingByOwnerUserUsername("testUser"))
                .thenReturn(meetingList);

        StepVerifier.create(meetingService.getCurrentUserSpacesuitData(principal))
                .expectNextMatches(dto -> dto.getId().equals(1L))
                .verifyComplete();

        verify(meetingRepository, times(1))
                .findAllMeetingByOwnerUserUsername("testUser");
    }

    @Test
    void deleteSpacesuitData_shouldDeleteSpacesuitData() {
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(meeting));

        StepVerifier.create(meetingService.deleteSpacesuitData(1L, principal))
                .verifyComplete();

        verify(meetingRepository, times(1)).delete(meeting);
    }

    @Test
    void deleteSpacesuitData_shouldThrowException_whenUserNotAuthorized() {
        meeting.getOwnerUser().setUsername("otherUser");

        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(meeting));

        StepVerifier.create(meetingService.deleteSpacesuitData(1L, principal))
                .expectError(IllegalArgumentException.class) // Expect error when user is not authorized
                .verify();

        verify(meetingRepository, never()).delete(meeting);
    }

    @Test
    void updateSpacesuitData_shouldUpdateAndReturnSpacesuitData() {
        UpdateMeetingDto updateDto = UpdateMeetingDto.builder().id(1L).fabricTextureId(1L).build();
        FabricTextureDto fabricTextureDto = FabricTextureDto.builder().id(1L).build();

        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(meeting));
        when(fabricTextureService.getFabricTextureById(1L, "token"))
                .thenReturn(Mono.just(fabricTextureDto));
        when(meetingRepository.save(any(Meeting.class)))
                .thenReturn(meeting);

        StepVerifier.create(meetingService.updateSpacesuitData(updateDto, principal, "token"))
                .expectNextMatches(dto -> dto.getId().equals(1L))
                .verifyComplete();

        verify(meetingRepository, times(1)).save(any(Meeting.class));
    }

    @Test
    void getSpacesuitData_shouldReturnSpacesuitData() {
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(meeting));

        StepVerifier.create(meetingService.getSpacesuitData(1L, principal))
                .expectNextMatches(dto -> dto.getId().equals(1L))
                .verifyComplete();

        verify(meetingRepository, times(1)).findById(1L);
    }

    @Test
    void getSpacesuitData_shouldThrowException_whenUserNotAuthorized() {
        meeting.getOwnerUser().setUsername("otherUser");

        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(meeting));

        StepVerifier.create(meetingService.getSpacesuitData(1L, principal))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
