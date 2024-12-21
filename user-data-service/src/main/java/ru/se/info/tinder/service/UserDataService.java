package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.CreateUserDataDto;
import ru.se.info.tinder.dto.UpdateUserDataDto;
import ru.se.info.tinder.dto.UserDataDto;
import ru.se.info.tinder.feign.LocationClient;
import ru.se.info.tinder.mapper.UserDataMapper;
import ru.se.info.tinder.model.Location;
import ru.se.info.tinder.model.UserData;
import ru.se.info.tinder.repository.UserDataRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;
import ru.se.info.tinder.service.exception.UserNotCompletedRegistrationException;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserDataService {
    private final UserDataRepository userDataRepository;
    private final LocationClient locationService;

    public Mono<UserDataDto> createUserData(CreateUserDataDto createUserDataDto, String token, Principal principal) {
        Mono<List<Location>> locations = locationService.getLocationsByIds(createUserDataDto.getLocations(), token)
                .map(l -> Location.builder().id(l.getId()).build())
                .collect(Collectors.toList());
        return locations.flatMap(
                (locationsList) -> {
                    if (userDataRepository.findUserDataByUsername(principal.getName()).isPresent()) {
                        return Mono.error(new IllegalArgumentException("User have already complete registration"));
                    }
                    UserData userData = UserDataMapper.toEntityUserData(createUserDataDto, new HashSet<>(locationsList));
                    return Mono.fromCallable(
                            () -> userDataRepository.save(userData)
                    ).map(UserDataMapper::toUserDataDto);
                }
        );
    }

    public Flux<UserDataDto> getAllUsersData() {
        return Flux.fromStream(
                () -> userDataRepository.findAll().stream()
        ).map(UserDataMapper::toUserDataDto);
    }

    public Flux<UserDataDto> getUsersDataByLocationId(Long locationId) {
        return Flux.fromStream(
                () -> userDataRepository.findAllUserDataByLocationsId(locationId).stream()
        ).map(UserDataMapper::toUserDataDto);
    }

    public Mono<UserDataDto> updateUserDataById(UpdateUserDataDto updateUserDataDto, Long id, Principal principal, String token) {
        Mono<List<Location>> locations = locationService.getLocationsByIds(updateUserDataDto.getLocations(), token)
                .map(l -> Location.builder().id(l.getId()).build())
                .collect(Collectors.toList());

        return locations.flatMap(
                (locationsSet) -> {
                    return Mono.fromCallable(
                            () -> userDataRepository.findById(id)
                    ).flatMap(
                            (oldUserData) -> {
                                if (oldUserData.isEmpty()) {
                                    return Mono.error(new NoEntityWithSuchIdException("UserData", id));
                                }
                                if (!oldUserData.get().getOwnerUser().getUsername().equals(principal.getName())) {
                                    return Mono.error(new IllegalArgumentException("User don't have enough rights for data updating"));
                                }
                                UserData newUserData = UserDataMapper.toEntityUserData(updateUserDataDto,
                                        new HashSet<>(locationsSet), oldUserData.get());
                                return Mono.fromCallable(
                                        () -> userDataRepository.save(newUserData)
                                ).map(UserDataMapper::toUserDataDto);
                            }
                    );
                }
        );
    }

    public Mono<UserDataDto> getUserDataDtoById(Long id) {
        return getUserDataById(id).map(UserDataMapper::toUserDataDto);
    }

    public Mono<Object> deleteUserDataById(Long id) {
        return Mono.fromRunnable(
                () -> userDataRepository.deleteById(id)
        );
    }

    protected Mono<UserData> getUserDataById(Long userDataId) {
        return Mono.fromCallable(() -> userDataRepository.findById(userDataId))
                .flatMap(
                        (userData) -> {
                            System.out.println(userData);
                            return userData.<Mono<? extends UserData>>map(Mono::just)
                                    .orElseGet(() -> Mono.error(new NoEntityWithSuchIdException("UserData", userDataId)));
                        }
                );
    }

    protected Mono<UserData> getUserDataByUsername(String username) {
        return Mono.fromCallable(
                () -> userDataRepository.findUserDataByUsername(username)
        ).flatMap(
                (userData) -> userData.<Mono<? extends UserData>>map(Mono::just)
                        .orElseGet(() -> Mono.error(() -> new UserNotCompletedRegistrationException(username)))
        );
    }

    protected void addProfileImageToUserData(Long userDataId, String id) {
        getUserDataById(userDataId)
                .flatMap(
                        (userData) -> {
                            userData.setProfileImageId(id);
                            return Mono.fromCallable(() ->
                                    userDataRepository.save(userData)
                            );
                        }
                );
    }

    public void deleteProfileImageToUserData(Long userDataId) {
        getUserDataById(userDataId)
                .flatMap(
                        (userData) -> {
                            userData.setProfileImageId(null);
                            return Mono.fromCallable(() ->
                                    userDataRepository.save(userData)
                            );
                        }
                );
    }
}
