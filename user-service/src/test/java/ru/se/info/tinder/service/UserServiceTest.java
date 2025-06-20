package ru.se.info.tinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.CreateUserDto;
import ru.se.info.tinder.dto.InfoDto;
import ru.se.info.tinder.dto.UpdateUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.feign.InfoServiceClient;
import ru.se.info.tinder.model.Location;
import ru.se.info.tinder.model.User;
import ru.se.info.tinder.model.UserData;
import ru.se.info.tinder.model.enums.Sex;
import ru.se.info.tinder.repository.UserRepository;
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

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InfoServiceClient infoServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserData_success() {
        CreateUserDto createUserDto = CreateUserDto.builder()
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

        InfoDto infoDto1 = InfoDto.builder().id(1L).build();
        InfoDto infoDto2 = InfoDto.builder().id(2L).build();
        List<InfoDto> infoDtos = List.of(infoDto1, infoDto2);

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

        when(infoServiceClient.getLocationsByIds(eq(createUserDto.getLocations()), anyString()))
                .thenReturn(infoDtos);
        when(userRepository.save(any(UserData.class))).thenReturn(savedUserData);
        UserDto result = userService.createUserData(createUserDto, "Bearer test-token", new Principal() {
            @Override
            public String getName() {
                return "admin";
            }
        });

        StepVerifier.create(Mono.just(result))
                .assertNext(userDto -> {
                    assertEquals("John", userDto.getFirstname());
                    assertEquals("Doe", userDto.getLastname());
                    assertEquals(LocalDate.of(1990, 1, 1), userDto.getBirthDate());
                    assertEquals(2, userDto.getLocations().size());
                })
                .verifyComplete();

        verify(infoServiceClient).getLocationsByIds(eq(createUserDto.getLocations()), eq("Bearer test-token"));
        verify(userRepository).save(argThat(savedUser -> {
            assertEquals("John", savedUser.getFirstname());
            assertEquals("Doe", savedUser.getLastname());
            assertEquals(2, savedUser.getLocations().size());
            return true;
        }));
    }


    @Test
    void getUserDataById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoEntityWithSuchIdException.class, () -> userService.getUserDataById(1L));
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

        UpdateUserDto updateUserDto = UpdateUserDto.builder()
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

        InfoDto infoDto1 = InfoDto.builder().id(1L).build();
        InfoDto infoDto2 = InfoDto.builder().id(2L).build();
        List<InfoDto> infoDtos = List.of(infoDto1, infoDto2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(oldUserData));
        when(infoServiceClient.getLocationsByIds(anyList(), anyString())).thenReturn(infoDtos);
        when(userRepository.save(any(UserData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto result = userService.updateUserDataById(updateUserDto, 1L, principal, "Bearer test-token");

        StepVerifier.create(Mono.just(result))
                .assertNext(userDto -> {
                    assertEquals("New Firstname", userDto.getFirstname());
                    assertEquals("New Lastname", userDto.getLastname());
                    assertEquals(LocalDate.of(1995, 5, 15), userDto.getBirthDate());
                    assertEquals(2, userDto.getLocations().size());
                })
                .verifyComplete();

        verify(userRepository).save(argThat(savedUserData -> {
            assertEquals("New Firstname", savedUserData.getFirstname());
            assertEquals("New Lastname", savedUserData.getLastname());
            assertEquals(2, savedUserData.getLocations().size());
            return true;
        }));

        verify(infoServiceClient).getLocationsByIds(anyList(), anyString());
    }


    @Test
    void deleteUserDataById_success() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUserDataById(1L);

        verify(userRepository).deleteById(1L);
    }
}
