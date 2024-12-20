package ru.se.info.tinder.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.se.info.tinder.dto.*;
import ru.se.info.tinder.model.UserEntity;
import ru.se.info.tinder.model.Roles;
import ru.se.info.tinder.model.enums.RoleName;
import ru.se.info.tinder.repository.UserRepository;
import ru.se.info.tinder.repository.RoleRepository;
import ru.se.info.tinder.service.UserService;
import ru.se.info.tinder.utils.JwtTokensUtils;

import java.security.Principal;
import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtTokensUtils jwtTokensUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Principal principal;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, roleRepository, jwtTokensUtils);
    }

    @Test
    public void testGetUserById_UserExists() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setUsername("testUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        Mono<UserDto> userDtoMono = userService.getUserById(userId);

        StepVerifier.create(userDtoMono)
                .expectNextMatches(userDto -> userDto.getId().equals(userId) && userDto.getUsername().equals("testUser"))
                .verifyComplete();

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testCreateUser_Success() {
        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setUsername("newUser");
        requestUserDto.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(requestUserDto.getUsername());
        userEntity.setPassword(requestUserDto.getPassword());

        Roles role = new Roles();
        role.setRoleName(RoleName.valueOf("USER"));

        when(userRepository.findByUsername(requestUserDto.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findRolesByRoleName(RoleName.valueOf("USER"))).thenReturn(role);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(requestUserDto.getPassword())).thenReturn("encodedPassword");

        Mono<UserEntity> createdUser = userService.createUser(requestUserDto);

        StepVerifier.create(createdUser)
                .expectNextMatches(user -> user.getUsername().equals(requestUserDto.getUsername()))
                .verifyComplete();
    }

    @Test
    public void testUpdateUserById_Success() {
        Long userId = 1L;
        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setUsername("updatedUser");

        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);
        existingUser.setUsername("existingUser");

        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(userId);
        updatedUser.setUsername("updatedUser");

        when(principal.getName()).thenReturn("existingUser");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(existingUser));
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);

        Mono<UserDto> result = userService.updateUserById(userId, requestUserDto, principal);

        StepVerifier.create(result)
                .expectNextMatches(userDto -> userDto.getUsername().equals("updatedUser"))
                .verifyComplete();
    }

//    @Test
//    public void testLoginUser_Success() {
//        RequestUserDto requestUserDto = new RequestUserDto();
//        requestUserDto.setUsername("testUser");
//        requestUserDto.setPassword("password");
//
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername(requestUserDto.getUsername());
//
//        String token = "jwtToken";
//        when(jwtTokensUtils.generateToken(userEntity)).thenReturn(token);
//        when(userRepository.findByUsername(requestUserDto.getUsername()))
//                .thenReturn(Optional.of(userEntity));
//
//        Mono<AuthUserDto> result = userService.loginUser(requestUserDto);
//
//        StepVerifier.create(result)
//                .expectNextMatches(authUserDto -> authUserDto.getToken().equals(token))
//                .verifyComplete();
//    }

    @Test
    public void testGetUserByUsername_UserExists() {
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        Mono<UserDto> result = userService.getUserByUsername(username);

        StepVerifier.create(result)
                .expectNextMatches(userDto -> userDto.getUsername().equals(username))
                .verifyComplete();
    }
}
