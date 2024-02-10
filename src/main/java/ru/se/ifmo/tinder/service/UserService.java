package ru.se.ifmo.tinder.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.LoginResponseDto;
import ru.se.ifmo.tinder.dto.UserDto;
import ru.se.ifmo.tinder.mapper.UserMapper;
import ru.se.ifmo.tinder.model.Roles;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.repository.RoleRepository;
import ru.se.ifmo.tinder.repository.UserDataRepository;
import ru.se.ifmo.tinder.repository.UserRepository;

import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDataRepository userDataRepository;
    private final RoleRepository roleRepository;

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
        // аутентификации
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

    public Integer addConnection(Principal principal, Integer user_id_2) {
        // Получаем id первого пользователя из Principal
        String username = principal.getName();
        Optional<User> user1 = userRepository.findByUsername(username);
        Integer userId1 = user1.get().getId();

        Integer userId2 = userRepository.findUserByUserDataId(user_id_2);

        // Вызываем метод репозитория для добавления связи
        return userRepository.addUserConnection(userId1, userId2);
    }


    public List<UserData> getConnections(Principal principal) {
        String username = principal.getName();
        Optional<User> user1 = userRepository.findByUsername(username);
        Integer userId1 = user1.get().getId();
        List<Integer> idList = userRepository.getUsersIdConnection(userId1);

        return userDataRepository.getListAllByUserDataIdInForConnections(idList);
    }
}
