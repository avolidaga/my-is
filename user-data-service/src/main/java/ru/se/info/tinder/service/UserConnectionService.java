package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.UserConnectionDto;
import ru.se.info.tinder.mapper.UserConnectionMapper;
import ru.se.info.tinder.model.UserConnection;
import ru.se.info.tinder.model.UserData;
import ru.se.info.tinder.repository.UserConnectionRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserConnectionService {

    private final UserConnectionRepository userConnectionRepository;
    private final UserDataService userDataService;

    public UserConnectionDto createConnection(Long userDataId, Principal principal) {
        UserData user1 = userDataService.getUserDataByUsername(principal.getName());
        UserData user2 = userDataService.getUserDataById(userDataId);

        UserConnection userConnection = UserConnection.builder()
                .userData1(user1)
                .userData2(user2)
                .matchDate(LocalDate.now())
                .build();

        UserConnection savedUserConnection = userConnectionRepository.save(userConnection);
        return UserConnectionMapper.toDtoUserConnection(savedUserConnection);
    }

    public List<UserConnectionDto> getConnections(Principal principal) {
        return userConnectionRepository.findAll().stream()
                .filter((connection) -> connection.getUserData1().getOwnerUser().getUsername().equals(principal.getName()) ||
                        connection.getUserData2().getOwnerUser().getUsername().equals(principal.getName()))
                .map(UserConnectionMapper::toDtoUserConnection).toList();
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