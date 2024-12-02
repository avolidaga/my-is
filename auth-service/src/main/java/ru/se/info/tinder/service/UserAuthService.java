package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.model.UserEntity;
import ru.se.info.tinder.repository.UserRepository;
import ru.se.info.tinder.service.exceptions.UserNotFoundException;

@RequiredArgsConstructor
@Service
public class UserAuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username)
        );
        return User
                .withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().getRoleName().name())
                .build();
    }
}