package ru.se.info.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.CreateUserDataDto;
import ru.se.info.tinder.dto.UpdateUserDataDto;
import ru.se.info.tinder.dto.UserDataDto;
import ru.se.info.tinder.feign.LocationClient;
import ru.se.info.tinder.mapper.LocationMapper;
import ru.se.info.tinder.mapper.UserDataMapper;
import ru.se.info.tinder.model.Location;
import ru.se.info.tinder.model.User;
import ru.se.info.tinder.model.UserData;
import ru.se.info.tinder.repository.UserDataRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;
import ru.se.info.tinder.service.exception.UserNotCompletedRegistrationException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserDataService {
    private final UserDataRepository userDataRepository;
    private final LocationClient locationService;

    @Transactional
    public UserDataDto createUserData(CreateUserDataDto createUserDataDto) {
        //User user = userService.getUserByUsername(principal.getName());
        //       TODO как-то брать из токена юзернэйм + настроить feign client на получение User
        User user = null; // заглушка

        Set<Location> locations = new HashSet<>(locationService.getLocationsByIds(createUserDataDto.getLocations())
                .getBody().stream()
                .map(LocationMapper::toEntityLocation)
                .collect(Collectors.toList()));

        UserData userData = UserDataMapper.toEntityUserData(createUserDataDto, locations);
        userData.setOwnerUser(user);
        UserData savedUserData = userDataRepository.save(userData);

        user.setUserData(savedUserData);
        // userService.saveUser(user);

        return UserDataMapper.toUserDataDto(savedUserData);
    }

    public Page<UserDataDto> getAllUsersData(Pageable pageable) {
        //User user = userService.getUserByUsername(principal.getName());
        //       TODO как-то брать из токена юзернэйм + настроить feign client на получение User
        User user = null; // заглушка
        Long userDataId = Optional.ofNullable(user.getUserData())
                .orElseThrow(() -> new UserNotCompletedRegistrationException(user.getUsername()))
                .getId();
        return userDataRepository.findAllUserDataExcludingUserId(userDataId, pageable)
                .map(UserDataMapper::toUserDataDto);
    }

    public Page<UserDataDto> getUsersDataByLocationId(Long locationId, Pageable pageable) {
        return userDataRepository.findAllUserDataByLocationsId(locationId, pageable)
                .map(UserDataMapper::toUserDataDto);
    }

    @Transactional
    public UserDataDto updateUserData(UpdateUserDataDto updateUserDataDto) {
        //User user = userService.getUserByUsername(principal.getName());
        //       TODO как-то брать из токена юзернэйм + настроить feign client на получение User
        User user = null; // заглушка

        Set<Location> locations = new HashSet<>(locationService.getLocationsByIds(updateUserDataDto.getLocations())
                .getBody().stream()
                .map(LocationMapper::toEntityLocation)
                .collect(Collectors.toList()));
        UserData oldUserData = userDataRepository.findById(updateUserDataDto.getId())
                .orElseThrow(() -> new NoEntityWithSuchIdException("UserData", updateUserDataDto.getId()));
        if (!oldUserData.getOwnerUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User don't have enough rights for data updating");
        }
        UserData newUserData = UserDataMapper.toEntityUserData(updateUserDataDto, locations, oldUserData);
        UserData savedUserData = userDataRepository.save(newUserData);
        return UserDataMapper.toUserDataDto(savedUserData);
    }

    public UserDataDto getUserDataByUser() {
        //User user = userService.getUserByUsername(principal.getName());
        //       TODO как-то брать из токена юзернэйм + настроить feign client на получение User
        User user = null; // заглушка

        UserData userData = Optional.ofNullable(user.getUserData())
                .orElseThrow(() -> new UserNotCompletedRegistrationException(user.getUsername()));
        return UserDataMapper.toUserDataDto(userData);
    }

    @Transactional
    public void deleteUserDataByUser() {
        //User user = userService.getUserByUsername(principal.getName());
        //       TODO как-то брать из токена юзернэйм + настроить feign client на получение User
        User user = null; // заглушка

        UserData userData = Optional.ofNullable(user.getUserData())
                .orElseThrow(() -> new UserNotCompletedRegistrationException(user.getUsername()));
        userDataRepository.delete(userData);
    }
}
