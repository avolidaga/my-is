package ru.se.ifmo.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.user_data.CreateUserDataDto;
import ru.se.ifmo.tinder.dto.user_data.UpdateUserDataDto;
import ru.se.ifmo.tinder.dto.user_data.UserDataDto;
import ru.se.ifmo.tinder.mapper.UserDataMapper;
import ru.se.ifmo.tinder.model.Location;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.repository.UserDataRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;
import ru.se.ifmo.tinder.service.exceptions.UserNotCompletedRegistrationException;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserDataService {
    private final UserDataRepository userDataRepository;
    private final UserService userService;
    private final LocationService locationService;


    @Transactional
    public UserDataDto createUserData(CreateUserDataDto createUserDataDto, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        Set<Location> locations = new HashSet<>(locationService.getLocationsByIds(createUserDataDto.getLocations()));

        UserData userData = UserDataMapper.toEntityUserData(createUserDataDto, locations);
        userData.setOwnerUser(user);
        UserData savedUserData = userDataRepository.save(userData);

        user.setUserData(savedUserData);
        userService.saveUser(user);

        return UserDataMapper.toUserDataDto(savedUserData);
    }

    public Page<UserDataDto> getAllUsersData(Principal principal, Pageable pageable) {
        User user = userService.getUserByUsername(principal.getName());
        Long userDataId = Optional.ofNullable(user.getUserData())
                .orElseThrow(() -> new UserNotCompletedRegistrationException(principal.getName()))
                .getId();
        return userDataRepository.findAllUserDataExcludingUserId(userDataId, pageable)
                .map(UserDataMapper::toUserDataDto);
    }

    public Page<UserDataDto> getUsersDataByLocationId(Long locationId, Pageable pageable) {
        return userDataRepository.findAllUserDataByLocationsId(locationId, pageable)
                .map(UserDataMapper::toUserDataDto);
    }

    @Transactional
    public UserDataDto updateUserData(UpdateUserDataDto updateUserDataDto, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        Set<Location> locations = new HashSet<>(locationService.getLocationsByIds(updateUserDataDto.getLocations()));
        UserData oldUserData = userDataRepository.findById(updateUserDataDto.getId())
                .orElseThrow(() -> new NoEntityWithSuchIdException("UserData", updateUserDataDto.getId()));
        if (!oldUserData.getOwnerUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User don't have enough rights for data updating");
        }
        UserData newUserData = UserDataMapper.toEntityUserData(updateUserDataDto, locations, oldUserData);
        UserData savedUserData = userDataRepository.save(newUserData);
        return UserDataMapper.toUserDataDto(savedUserData);
    }

    public UserDataDto getUserDataByUser(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        UserData userData = Optional.ofNullable(user.getUserData())
                .orElseThrow(() -> new UserNotCompletedRegistrationException(principal.getName()));
        return UserDataMapper.toUserDataDto(userData);
    }

    @Transactional
    public void deleteUserDataByUser(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        UserData userData = Optional.ofNullable(user.getUserData())
                .orElseThrow(() -> new UserNotCompletedRegistrationException(principal.getName()));
        userDataRepository.delete(userData);
    }
}
