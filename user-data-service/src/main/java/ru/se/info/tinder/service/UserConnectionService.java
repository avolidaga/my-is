package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.dto.user_connection.UserConnectionDto;
import ru.se.ifmo.tinder.mapper.UserConnectionMapper;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserConnection;
import ru.se.ifmo.tinder.repository.UserConnectionRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserConnectionService {

    private final UserService userService;
    private final UserConnectionRepository userConnectionRepository;

    @Transactional
    public UserConnectionDto createConnection(Principal principal, Long userDataId) {
        User user1 = userService.getUserByUsername(principal.getName());
        User user2 = userService.getUserByUserDataId(userDataId);

        UserConnection userConnection = userConnectionRepository.save(UserConnection.builder()
                .user1(user1)
                .user2(user2)
                .matchDate(LocalDate.now())
                .build());
        return UserConnectionMapper.toDtoUserConnection(userConnection);
    }

    public List<UserConnectionDto> getConnections(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
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
        User user = userService.getUserByUsername(principal.getName());
        UserConnection userConnection = userConnectionRepository.findById(connectionId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Connection", connectionId));

        if (!userConnection.getUser1().getId().equals(user.getId()) && !userConnection.getUser2().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User don't have enough rights for connection getting");
        }
        return UserConnectionMapper.toDtoUserConnection(userConnection);
    }
}
