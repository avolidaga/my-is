package ru.se.ifmo.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.user.AuthUserDto;
import ru.se.ifmo.tinder.dto.user.CreateUserDto;
import ru.se.ifmo.tinder.dto.user.LoginUserDto;
import ru.se.ifmo.tinder.mapper.UserMapper;
import ru.se.ifmo.tinder.model.Roles;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.enums.RoleName;
import ru.se.ifmo.tinder.repository.RoleRepository;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;
import ru.se.ifmo.tinder.service.exceptions.UserNotFoundException;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public void createUser(CreateUserDto createUserDto) {
        User user = UserMapper.toEntityUser(createUserDto);
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with such username already exist");
        }
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        Roles role = roleRepository.findRolesByRoleName(RoleName.USER);
        user.setUserData(null);
        user.setRole(role);
        userRepository.save(user);
    }

    public AuthUserDto loginUser(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(), loginUserDto.getPassword())
        );
        User user = userRepository.findByUsername(loginUserDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException(loginUserDto.getUsername()));
        String credentials = loginUserDto.getUsername() + ":" + loginUserDto.getPassword();
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
