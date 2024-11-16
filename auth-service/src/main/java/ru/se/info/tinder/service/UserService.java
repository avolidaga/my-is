package ru.se.info.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.AuthUserDto;
import ru.se.info.tinder.dto.RequestUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.mapper.UserMapper;
import ru.se.info.tinder.model.Roles;
import ru.se.info.tinder.model.User;
import ru.se.info.tinder.model.RoleName;
import ru.se.info.tinder.repository.RoleRepository;
import ru.se.info.tinder.repository.UserRepository;
import ru.se.info.tinder.service.exceptions.NoEntityWithSuchIdException;
import ru.se.info.tinder.service.exceptions.UserNotFoundException;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public void deleteUserById(Long userId, Principal principal) {
        User user = getUserByUsername(principal.getName());
        if (!user.getId().equals(userId)) {
            throw new IllegalArgumentException("User don't have enough rights for user deleting");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserDto updateUserById(Long userId, RequestUserDto requestUserDto, Principal principal) {
        User user = getUserByUsername(principal.getName());
        if (!user.getId().equals(userId)) {
            throw new IllegalArgumentException("User don't have enough rights for user updating");
        }
        User newUser = UserMapper.toEntityUser(requestUserDto, user);
        User savedUser = userRepository.save(newUser);
        return UserMapper.toDtoUser(savedUser);
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User", userId));
        return UserMapper.toDtoUser(user);
    }

    @Transactional
    public void createUser(RequestUserDto requestUserDto) {
        User user = UserMapper.toEntityUser(requestUserDto);
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with such username already exist");
        }
        user.setPassword(passwordEncoder.encode(requestUserDto.getPassword()));
        Roles role = roleRepository.findRolesByRoleName(RoleName.USER);
        user.setUserData(null);
        user.setRole(role);
        userRepository.save(user);
    }

    public AuthUserDto loginUser(RequestUserDto requestUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestUserDto.getUsername(), requestUserDto.getPassword())
        );
        User user = getUserByUsername(requestUserDto.getUsername());
        String credentials = requestUserDto.getUsername() + ":" + requestUserDto.getPassword();
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return UserMapper.toDtoAuthUser(user, base64Credentials);
    }

    protected User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    protected User getUserByUserDataId(Long id) {
        return userRepository.findUserByUserDataId(id)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User", "User data", id));
    }

    protected User saveUser(User user) {
        return userRepository.save(user);
    }
}
