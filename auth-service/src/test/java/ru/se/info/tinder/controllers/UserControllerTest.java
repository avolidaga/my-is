package ru.se.info.tinder.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.AuthUserDto;
import ru.se.info.tinder.dto.RequestUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.model.UserEntity;
import ru.se.info.tinder.service.UserService;

import java.security.Principal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

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
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("encodedPassword");

        when(userService.createUser(requestUserDto)).thenReturn(Mono.just(userEntity));

        Mono<UserDto> result = userController.createUser(requestUserDto);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getUsername().equals("testUser"))
                .verifyComplete();

        verify(userService, times(1)).createUser(requestUserDto);
    }

    @Test
    void testGetUserById() {
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(Mono.just(userDto));

        Mono<UserDto> result = userController.getUserById(userId);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getUsername().equals("testUser"))
                .verifyComplete();

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testUpdateUserById() {
        Long userId = 1L;
        when(userService.updateUserById(userId, requestUserDto, principal)).thenReturn(Mono.just(userDto));

        Mono<UserDto> result = userController.updateUserById(userId, requestUserDto, principal);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getUsername().equals("testUser"))
                .verifyComplete();

        verify(userService, times(1)).updateUserById(userId, requestUserDto, principal);
    }

    @Test
    void testDeleteUserById() {
        Long userId = 1L;
        when(userService.deleteUserById(userId, principal)).thenReturn(Mono.empty());

        Mono<Void> result = userController.deleteUserById(userId, principal);

        StepVerifier.create(result)
                .verifyComplete();

        verify(userService, times(1)).deleteUserById(userId, principal);
    }

    @Test
    void testLoginUser() {
        when(userService.loginUser(requestUserDto)).thenReturn(Mono.just(authUserDto));

        Mono<AuthUserDto> result = userController.loginUser(requestUserDto);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getToken().equals("some-jwt-token"))
                .verifyComplete();

        verify(userService, times(1)).loginUser(requestUserDto);
    }
}
