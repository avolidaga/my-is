package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.dto.user_connection.UserConnectionDto;
import ru.se.ifmo.tinder.mapper.UserConnectionMapper;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserConnection;
import ru.se.ifmo.tinder.repository.UserConnectionRepository;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;
import ru.se.ifmo.tinder.service.exceptions.UserNotFoundException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserConnectionService {

    private final UserRepository userRepository;
    private final UserConnectionRepository userConnectionRepository;

    @Transactional
    public UserConnectionDto createConnection(Principal principal, Long userDataId) {
        String username = principal.getName();
        User user1 = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        User user2 = userRepository.findUserByUserDataId(userDataId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User", "User data", userDataId));


        UserConnection userConnection = userConnectionRepository.save(UserConnection.builder()
                .user1(user1)
                .user2(user2)
                .matchDate(LocalDate.now())
                .build());
        return UserConnectionMapper.toDtoUserConnection(userConnection);
    }

    public List<UserConnectionDto> getConnections(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Set<UserConnectionDto> userConnections = new HashSet<>();

        for (UserConnection connection : userConnectionRepository.findAll()) {
            if (connection.getUser1().equals(user)) {
                userConnections.add(UserConnectionMapper.toDtoUserConnection(connection));
            } else if (connection.getUser2().equals(user)) {
                userConnections.add(UserConnectionMapper.toDtoUserConnection(connection));
            }
        }
        return new ArrayList<>(userConnections);
    }

    public UserConnectionDto getUserConnectionById(Long connectionId, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        UserConnection userConnection = userConnectionRepository.findById(connectionId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Connection", connectionId));

        if (!userConnection.getUser1().getId().equals(user.getId()) && !userConnection.getUser2().getId().equals(user.getId())) {
            throw new IllegalArgumentException(); // TODO кастомную ошибку или сообщение подробное
        }
        return UserConnectionMapper.toDtoUserConnection(userConnection);
    }
}
