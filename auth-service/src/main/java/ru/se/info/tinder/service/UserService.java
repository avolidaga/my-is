package ru.se.info.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.AuthUserDto;
import ru.se.info.tinder.dto.RequestUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.mapper.UserMapper;
import ru.se.info.tinder.model.Roles;
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
    private final UserAuthService userAuthService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokensUtils jwtTokensUtils;

    public void deleteUserById(Long userId, Principal principal) {
        UserEntity userEntity = getUserEntityByUsername(principal.getName());
        if (!userEntity.getId().equals(userId)) {
            throw new IllegalArgumentException("User don't have enough rights for user deleting");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserDto updateUserById(Long userId, RequestUserDto requestUserDto, Principal principal) {
        UserEntity userEntity = getUserEntityByUsername(principal.getName());
        if (!userEntity.getId().equals(userId)) {
            throw new IllegalArgumentException("User don't have enough rights for user updating");
        }
        UserEntity newUserEntity = UserMapper.toEntityUser(requestUserDto, userEntity);
        UserEntity savedUserEntity = userRepository.save(newUserEntity);
        return UserMapper.toDtoUser(savedUserEntity);
    }

    public UserDto getUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User", userId));
        return UserMapper.toDtoUser(userEntity);
    }

    @Transactional
    public void createUser(RequestUserDto requestUserDto) {
        UserEntity userEntity = UserMapper.toEntityUser(requestUserDto);
        if (userRepository.findByUsername(userEntity.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with such username already exist");
        }
        userEntity.setPassword(passwordEncoder.encode(requestUserDto.getPassword()));
        Roles role = roleRepository.findRolesByRoleName(RoleName.USER);
        userEntity.setUserData(null);
        userEntity.setRole(role);
        userRepository.save(userEntity);
    }

    public AuthUserDto loginUser(RequestUserDto requestUserDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestUserDto.getUsername(), requestUserDto.getPassword()));
        UserDetails userDetails = userAuthService.loadUserByUsername(requestUserDto.getUsername());
        return new AuthUserDto(jwtTokensUtils.generateToken(userDetails));
    }

    public UserDto getUserByUsername(String username) {
        return UserMapper.toDtoUser(getUserEntityByUsername(username));
    }

    protected UserEntity getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    protected UserEntity getUserByUserDataId(Long id) {
        return userRepository.findUserByUserDataId(id)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User", "User data", id));
    }

    protected UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
