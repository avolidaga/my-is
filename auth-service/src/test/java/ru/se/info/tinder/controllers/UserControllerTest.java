package ru.se.info.tinder.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.AuthUserDto;
import ru.se.info.tinder.dto.RequestUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.model.UserEntity;
import ru.se.info.tinder.service.UserService;
import ru.se.info.tinder.utils.JwtTokensUtils;

import java.security.Principal;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService; // Заменяем @MockBean на @Mock

    @InjectMocks
    private UserController userController;

    private RequestUserDto requestUserDto;
    private UserDto userDto;
    private AuthUserDto authUserDto;
    private Principal principal;

    @BeforeEach
    void setUp() {
        requestUserDto = new RequestUserDto();
        requestUserDto.setUsername("testUser");
        requestUserDto.setPassword("password");

        userDto = new UserDto();
        userDto.setUsername("testUser");

        authUserDto = new AuthUserDto("some-jwt-token");


    }

    @Test
    void testCreateUser() {
        // Mock the service layer to return Mono<UserEntity>
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("encodedPassword");

        when(userService.createUser(requestUserDto)).thenReturn(Mono.just(userEntity));

        // When
        Mono<UserDto> result = userController.createUser(requestUserDto);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.getUsername().equals("testUser") // Validate the transformation to DTO
                )
                .verifyComplete();

        // Verify that the service was called once
        verify(userService, times(1)).createUser(requestUserDto);
    }

    @Test
    void testGetUserById() {
        // Given
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(Mono.just(userDto));

        // When
        Mono<UserDto> result = userController.getUserById(userId);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getUsername().equals("testUser"))
                .verifyComplete();

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testUpdateUserById() {
        // Given
        Long userId = 1L;
        when(userService.updateUserById(userId, requestUserDto, principal)).thenReturn(Mono.just(userDto));

        // When
        Mono<UserDto> result = userController.updateUserById(userId, requestUserDto, principal);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getUsername().equals("testUser"))
                .verifyComplete();

        verify(userService, times(1)).updateUserById(userId, requestUserDto, principal);
    }

    @Test
    void testDeleteUserById() {
        // Given
        Long userId = 1L;
        when(userService.deleteUserById(userId, principal)).thenReturn(Mono.empty());

        // When
        Mono<Void> result = userController.deleteUserById(userId, principal);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(userService, times(1)).deleteUserById(userId, principal);
    }

    @Test
    void testLoginUser() {
        // Given
        when(userService.loginUser(requestUserDto)).thenReturn(Mono.just(authUserDto));

        // When
        Mono<AuthUserDto> result = userController.loginUser(requestUserDto);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getToken().equals("some-jwt-token"))
                .verifyComplete();

        verify(userService, times(1)).loginUser(requestUserDto);
    }
}
