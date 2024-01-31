package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.LoginResponseDto;
import ru.se.ifmo.tinder.dto.UserDto;
import ru.se.ifmo.tinder.mapper.UserMapper;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.repository.UserRepository;

import java.util.Base64;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    public void createUser(UserDto userDto){
        User user = UserMapper.toEntityUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }
    public LoginResponseDto login(UserDto authRequest) {
        // аутентификации
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        String credentials = authRequest.getUsername() + ":" + authRequest.getPassword();
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return LoginResponseDto.builder()
                .credentials(base64Credentials)
                .build();
    }
}
