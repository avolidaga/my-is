package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.se.info.tinder.dto.UserConnectionDto;
import ru.se.info.tinder.mapper.UserConnectionMapper;
import ru.se.info.tinder.model.UserConnection;
import ru.se.info.tinder.model.UserData;
import ru.se.info.tinder.repository.UserConnectionRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserConnectionService {

    private final UserConnectionRepository userConnectionRepository;
    private final UserDataService userDataService;

    @Transactional
    public UserConnectionDto createConnection(Long userDataId, Principal principal) {
        UserData user1 = userDataService.getUserDataByUsername(principal.getName());
        UserData user2 = userDataService.getUserDataById(userDataId);

        UserConnection userConnection = userConnectionRepository.save(UserConnection.builder()
                .userData1(user1)
                .userData2(user2)
                .matchDate(LocalDate.now())
                .build());
        return UserConnectionMapper.toDtoUserConnection(userConnection);
    }

    public List<UserConnectionDto> getConnections(Principal principal) {
        Set<UserConnectionDto> userConnections = new HashSet<>();

        for (UserConnection connection : userConnectionRepository.findAll()) {
            if (connection.getUserData1().getOwnerUser().getUsername().equals(principal.getName())) {
                userConnections.add(UserConnectionMapper.toDtoUserConnection(connection));
            } else if (connection.getUserData2().getOwnerUser().getUsername().equals(principal.getName())) {
                userConnections.add(UserConnectionMapper.toDtoUserConnection(connection));
            }
        }
        return new ArrayList<>(userConnections);
    }

    public UserConnectionDto getUserConnectionById(Long connectionId, Principal principal) {
        UserConnection userConnection = userConnectionRepository.findById(connectionId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Connection", connectionId));

        if (!userConnection.getUserData1().getOwnerUser().getUsername().equals(principal.getName()) &&
                !userConnection.getUserData2().getOwnerUser().getUsername().equals(principal.getName())) {
            throw new IllegalArgumentException("User don't have enough rights for connection getting");
        }
        return UserConnectionMapper.toDtoUserConnection(userConnection);
    }
}
