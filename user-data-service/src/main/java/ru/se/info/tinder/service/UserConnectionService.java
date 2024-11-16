package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.se.info.tinder.dto.UserConnectionDto;
import ru.se.info.tinder.mapper.UserConnectionMapper;
import ru.se.info.tinder.model.User;
import ru.se.info.tinder.model.UserConnection;
import ru.se.info.tinder.repository.UserConnectionRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserConnectionService {

    private final UserConnectionRepository userConnectionRepository;

    @Transactional
    public UserConnectionDto createConnection(Long userDataId) {
//        User user1 = userService.getUserByUsername(principal.getName());
//        User user2 = userService.getUserByUserDataId(userDataId);
//        TODO как-то брать из токена юзернэйм + настроить feign client на получение User
        User user1 = null;
        User user2 = null; // ленивые заглушки

        UserConnection userConnection = userConnectionRepository.save(UserConnection.builder()
                .user1(user1)
                .user2(user2)
                .matchDate(LocalDate.now())
                .build());
        return UserConnectionMapper.toDtoUserConnection(userConnection);
    }

    public List<UserConnectionDto> getConnections() {
        // User user = userService.getUserByUsername(principal.getName());
        //       TODO как-то брать из токена юзернэйм + настроить feign client на получение User
        User user = null; // заглушка
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

    public UserConnectionDto getUserConnectionById(Long connectionId) {
        //User user = userService.getUserByUsername(principal.getName());
        //       TODO как-то брать из токена юзернэйм + настроить feign client на получение User
        User user = null; // заглушка

        UserConnection userConnection = userConnectionRepository.findById(connectionId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Connection", connectionId));

        if (!userConnection.getUser1().getId().equals(user.getId()) && !userConnection.getUser2().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User don't have enough rights for connection getting");
        }
        return UserConnectionMapper.toDtoUserConnection(userConnection);
    }
}
