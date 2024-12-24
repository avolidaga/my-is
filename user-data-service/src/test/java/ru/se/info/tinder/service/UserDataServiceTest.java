package ru.se.info.tinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.CreateUserDataDto;
import ru.se.info.tinder.dto.LocationDto;
import ru.se.info.tinder.dto.UpdateUserDataDto;
import ru.se.info.tinder.dto.UserDataDto;
import ru.se.info.tinder.feign.LocationClient;
import ru.se.info.tinder.model.Location;
import ru.se.info.tinder.model.User;
import ru.se.info.tinder.model.UserData;
import ru.se.info.tinder.model.enums.Sex;
import ru.se.info.tinder.repository.UserDataRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserDataServiceTest {

    @InjectMocks
    private UserDataService userDataService;

    @Mock
    private UserDataRepository userDataRepository;

    @Mock
    private LocationClient locationClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserData_success() {
        CreateUserDataDto createUserDataDto = CreateUserDataDto.builder()
                .userId(1L)
                .birthDate(LocalDate.of(1990, 1, 1))
                .firstname("John")
                .lastname("Doe")
                .sex(Sex.MEN)
                .weight(70)
                .height(180)
                .hairColor("Black")
                .locations(List.of(1L, 2L))
                .build();

        LocationDto locationDto1 = LocationDto.builder().id(1L).build();
        LocationDto locationDto2 = LocationDto.builder().id(2L).build();
        List<LocationDto> locationDtos = List.of(locationDto1, locationDto2);

        Location location1 = Location.builder().id(1L).build();
        Location location2 = Location.builder().id(2L).build();
        List<Location> locations = List.of(location1, location2);

        UserData savedUserData = UserData.builder()
                .id(1L)
                .ownerUser(User.builder().id(1L).username("john_doe").build())
                .firstname("John")
                .lastname("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .sex(Sex.MEN)
                .weight(70)
                .height(180)
                .hairColor("Black")
                .locations(new HashSet<>(locations))
                .build();

        when(locationClient.getLocationsByIds(eq(createUserDataDto.getLocations()), anyString()))
                .thenReturn(locationDtos);
        when(userDataRepository.save(any(UserData.class))).thenReturn(savedUserData);
        UserDataDto result = userDataService.createUserData(createUserDataDto, "Bearer test-token", new Principal() {
            @Override
            public String getName() {
                return "admin";
            }
        });

        StepVerifier.create(Mono.just(result))
                .assertNext(userDataDto -> {
                    assertEquals("John", userDataDto.getFirstname());
                    assertEquals("Doe", userDataDto.getLastname());
                    assertEquals(LocalDate.of(1990, 1, 1), userDataDto.getBirthDate());
                    assertEquals(2, userDataDto.getLocations().size());
                })
                .verifyComplete();

        verify(locationClient).getLocationsByIds(eq(createUserDataDto.getLocations()), eq("Bearer test-token"));
        verify(userDataRepository).save(argThat(savedUser -> {
            assertEquals("John", savedUser.getFirstname());
            assertEquals("Doe", savedUser.getLastname());
            assertEquals(2, savedUser.getLocations().size());
            return true;
        }));
    }


    @Test
    void getUserDataById_notFound() {
        when(userDataRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoEntityWithSuchIdException.class, () -> userDataService.getUserDataById(1L));
    }

    @Test
    void testUpdateUserDataById() {
        User existingUser = User.builder()
                .id(1L)
                .username("jane_doe")
                .password("hashed_password")
                .build();

        UserData oldUserData = UserData.builder()
                .id(1L)
                .ownerUser(existingUser) // Установка существующего пользователя
                .firstname("Old Firstname")
                .lastname("Old Lastname")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        existingUser.setUserData(oldUserData);

        UpdateUserDataDto updateUserDataDto = UpdateUserDataDto.builder()
                .firstname("New Firstname")
                .lastname("New Lastname")
                .birthDate(LocalDate.of(1995, 5, 15))
                .sex(Sex.WOMEN)
                .weight(60)
                .height(170)
                .hairColor("Brown")
                .locations(List.of(1L, 2L))
                .build();

        Location location1 = Location.builder().id(1L).build();
        Location location2 = Location.builder().id(2L).build();
        List<Location> locations = List.of(location1, location2);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("jane_doe");

        LocationDto locationDto1 = LocationDto.builder().id(1L).build();
        LocationDto locationDto2 = LocationDto.builder().id(2L).build();
        List<LocationDto> locationDtos = List.of(locationDto1, locationDto2);

        when(userDataRepository.findById(1L)).thenReturn(Optional.of(oldUserData));
        when(locationClient.getLocationsByIds(anyList(), anyString())).thenReturn(locationDtos);
        when(userDataRepository.save(any(UserData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDataDto result = userDataService.updateUserDataById(updateUserDataDto, 1L, principal, "Bearer test-token");

        StepVerifier.create(Mono.just(result))
                .assertNext(userDataDto -> {
                    assertEquals("New Firstname", userDataDto.getFirstname());
                    assertEquals("New Lastname", userDataDto.getLastname());
                    assertEquals(LocalDate.of(1995, 5, 15), userDataDto.getBirthDate());
                    assertEquals(2, userDataDto.getLocations().size());
                })
                .verifyComplete();

        verify(userDataRepository).save(argThat(savedUserData -> {
            assertEquals("New Firstname", savedUserData.getFirstname());
            assertEquals("New Lastname", savedUserData.getLastname());
            assertEquals(2, savedUserData.getLocations().size());
            return true;
        }));

        verify(locationClient).getLocationsByIds(anyList(), anyString());
    }


    @Test
    void deleteUserDataById_success() {
        doNothing().when(userDataRepository).deleteById(1L);

        userDataService.deleteUserDataById(1L);

        verify(userDataRepository).deleteById(1L);
    }
}
