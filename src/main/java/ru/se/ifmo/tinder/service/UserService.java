package ru.se.ifmo.tinder.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.dto.LoginResponseDto;
import ru.se.ifmo.tinder.dto.UserDto;
import ru.se.ifmo.tinder.mapper.UserMapper;
import ru.se.ifmo.tinder.model.Roles;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserConnection;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.repository.RoleRepository;
import ru.se.ifmo.tinder.repository.UserConnectionRepository;
import ru.se.ifmo.tinder.repository.UserDataRepository;
import ru.se.ifmo.tinder.repository.UserRepository;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDataRepository userDataRepository;
    private final RoleRepository roleRepository;
    private final UserConnectionRepository userConnectionRepository;

    public boolean createUser(UserDto userDto) {
        User user = UserMapper.toEntityUser(userDto);
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Roles role = roleRepository.findRolesByRoleName("user");
        user.setUser_data_id(null);
        user.setRole(role);
        userRepository.save(user);
        return true;
    }

    public LoginResponseDto login(UserDto authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        String credentials = authRequest.getUsername() + ":" + authRequest.getPassword();
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found username: %s".formatted(authRequest.getUsername())));
        return LoginResponseDto.builder()
                .credentials(base64Credentials)
                .role(user.getRole().getRoleName())
                .build();
    }

    @Transactional
    public Integer addConnection(Principal principal, Integer user_id_2) {
        String username = principal.getName();
        Optional<User> user1 = userRepository.findByUsername(username);
        Integer userId1 = user1.get().getId();

        Integer userId2 = userRepository.findUserByUserDataId(user_id_2);

        return userConnectionRepository.insertUserConnection(userId1, userId2);

    }



    public List<User> getConnections(Principal principal) {
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);
        Set<User> userConnections = new HashSet<>();
        Set<User> mutualConnections = new HashSet<>();

        for (UserConnection connection : userConnectionRepository.findAll()) {
            if (connection.getUser1().equals(user)) {
                userConnections.add(connection.getUser2());
            } else if (connection.getUser2().equals(user)) {
                userConnections.add(connection.getUser1());
            }
        }

        for (UserConnection connection : userConnectionRepository.findAll()) {
            if (userConnections.contains(connection.getUser1()) && connection.getUser2().equals(user)) {
                mutualConnections.add(connection.getUser1());
            } else if (userConnections.contains(connection.getUser2()) && connection.getUser1().equals(user)) {
                mutualConnections.add(connection.getUser2());
            }
        }

        return new ArrayList<>(mutualConnections);
    }
}
