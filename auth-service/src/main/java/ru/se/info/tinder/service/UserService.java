package ru.se.info.tinder.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.AuthUserDto;
import ru.se.info.tinder.dto.RequestUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.mapper.UserMapper;
import ru.se.info.tinder.model.UserEntity;
import ru.se.info.tinder.model.enums.RoleName;
import ru.se.info.tinder.repository.RoleRepository;
import ru.se.info.tinder.repository.UserRepository;
import ru.se.info.tinder.service.exceptions.NoEntityWithSuchIdException;
import ru.se.info.tinder.service.exceptions.UserNotFoundException;
import ru.se.info.tinder.utils.JwtTokensUtils;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokensUtils jwtTokensUtils;

    public Mono<UserDto> updateUserById(Long userId, RequestUserDto requestUserDto, Principal principal) {
        return getUserEntityByUsername(principal.getName())
                .flatMap(
                        user ->
                        {
                            if (!user.getId().equals(userId)) {
                                return Mono.error(new IllegalArgumentException("User don't have enough rights for user updating"));
                            }
                            UserEntity newUserEntity = UserMapper.toEntityUser(requestUserDto, user);
                            return Mono.fromCallable(
                                    () -> userRepository.save(newUserEntity)
                            ).map(UserMapper::toDtoUser);
                        }
                );
    }

    public Mono<UserDto> getUserById(Long userId) {
        return Mono.fromCallable(
                        () -> userRepository.findById(userId)
                                .orElseThrow(() -> new NoEntityWithSuchIdException("User", userId))
                ).map(UserMapper::toDtoUser);
    }

    public Mono<UserEntity> createUser(RequestUserDto requestUserDto) {

        return Mono.fromCallable(
                () -> userRepository.findByUsername(requestUserDto.getUsername())
        ).flatMap(
                existingUser -> {
                    if (existingUser.isPresent()) {
                        return Mono.error(new IllegalArgumentException("User with such username already exist"));
                    } else {
                        return Mono.fromCallable(
                                () -> roleRepository.findRolesByRoleName(RoleName.USER)
                        ).flatMap(
                                role -> {
                                    UserEntity userEntity = UserMapper.toEntityUser(requestUserDto);
                                    userEntity.setPassword(passwordEncoder.encode(requestUserDto.getPassword()));
                                    userEntity.setRole(role);
                                    return Mono.fromCallable(
                                            () -> userRepository.save(userEntity)
                                    );
                                }
                        );
                    }
                }
        );
    }

    public Mono<AuthUserDto> loginUser(RequestUserDto requestUserDto) {
        return getUserEntityByUsername(requestUserDto.getUsername())
                .flatMap(
                        user -> Mono.fromCallable(
                                () -> new AuthUserDto(jwtTokensUtils.generateToken(user))
                        )
                );
    }

    public Mono<UserDto> getUserByUsername(String username) {
        return getUserEntityByUsername(username).map(UserMapper::toDtoUser);
    }

    protected Mono<UserEntity> getUserEntityByUsername(String username) {
        return Mono.fromCallable(
                () -> userRepository.findByUsername(username)
        ).flatMap(
                (user) -> {
                    if (user.isPresent()) {
                        return Mono.just(user.get());
                    }
                    return Mono.error(new UserNotFoundException(username));
                }
        );
    }

    protected Mono<UserEntity> getUserByUserDataId(Long id) {
        return Mono.fromCallable(
                () -> userRepository.findUserByUserDataId(id)
        ).flatMap(
                (user) -> {
                    if (user.isPresent()) {
                        return Mono.just(user.get());
                    }
                    return Mono.error(new NoEntityWithSuchIdException("User", "User data", id));
                }
        );
    }

    public Mono<Void> deleteUserById(Long userId, Principal principal) {
        return getUserEntityByUsername(principal.getName())
                .flatMap(
                        user -> Mono.fromRunnable(
                                () -> userRepository.deleteById(userId)
                        )
                );
    }

    protected Mono<UserEntity> saveUser(UserEntity userEntity) {
        return Mono.fromCallable(
                () -> userRepository.save(userEntity)
        );
    }
}
