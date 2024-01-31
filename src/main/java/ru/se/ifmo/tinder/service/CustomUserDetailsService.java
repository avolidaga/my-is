package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.service.exceptions.UserNotFoundException;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username: %s not found".formatted(username))
        );
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}