package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.UserConnectionDto;
import ru.se.info.tinder.mapper.UserConnectionMapper;
import ru.se.info.tinder.model.UserConnection;
import ru.se.info.tinder.repository.UserConnectionRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.security.Principal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class UserConnectionService {

    private final UserConnectionRepository userConnectionRepository;
    private final UserDataService userDataService;

    public Mono<UserConnectionDto> createConnection(Long userDataId, Principal principal) {
        return userDataService.getUserDataByUsername(principal.getName())
                .flatMap(user1 -> userDataService.getUserDataById(userDataId)
                        .flatMap(user2 -> {
                            UserConnection userConnection = UserConnection.builder()
                                    .userData1(user1)
                                    .userData2(user2)
                                    .matchDate(LocalDate.now())
                                    .build();

                            return Mono.fromCallable(
                                    () -> userConnectionRepository.save(userConnection)
                            ).map(UserConnectionMapper::toDtoUserConnection);
                        })
                );
    }

    public Flux<UserConnectionDto> getConnections(Principal principal) {
        return Flux.fromStream(
                        () -> userConnectionRepository.findAll().stream()
                ).filter((connection) -> connection.getUserData1().getOwnerUser().getUsername().equals(principal.getName()) ||
                        connection.getUserData2().getOwnerUser().getUsername().equals(principal.getName()))
                .map(UserConnectionMapper::toDtoUserConnection);
    }

    public Mono<UserConnectionDto> getUserConnectionById(Long connectionId, Principal principal) {
        return Mono.fromCallable(
                () -> userConnectionRepository.findById(connectionId)
                        .orElseThrow(() -> new NoEntityWithSuchIdException("Connection", connectionId))
        ).flatMap(
                (userConnection) -> {
                    if (!userConnection.getUserData1().getOwnerUser().getUsername().equals(principal.getName()) &&
                            !userConnection.getUserData2().getOwnerUser().getUsername().equals(principal.getName())) {
                        return Mono.error(new IllegalArgumentException("User don't have enough rights for connection getting"));
                    }
                    return Mono.just(userConnection)
                            .map(UserConnectionMapper::toDtoUserConnection);
                }
        );
    }
}
