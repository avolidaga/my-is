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
import ru.se.ifmo.tinder.repository.LocationRepository;
import ru.se.ifmo.tinder.repository.UserDataRepository;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;
import ru.se.ifmo.tinder.service.exceptions.UserNotCompletedRegistrationException;
import ru.se.ifmo.tinder.service.exceptions.UserNotFoundException;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserDataService {
    private final UserDataRepository userDataRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;


    @Transactional
    public UserDataDto createUserData(CreateUserDataDto createUserDataDto, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Set<Location> locations = new HashSet<>(locationRepository.findAllById(createUserDataDto.getLocations()));
        if (locations.isEmpty()) throw new NoEntityWithSuchIdException("Location", createUserDataDto.getLocations());
        UserData userData = UserDataMapper.toEntityUserData(createUserDataDto, locations);
        userData.setOwnerUser(user);
        UserData savedUserData = userDataRepository.save(userData);

        user.setUserData(savedUserData);
        userRepository.save(user);

        return UserDataMapper.toUserDataDto(savedUserData);
    }

    public Page<UserDataDto> getAllUsersData(Principal principal, Pageable pageable) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Long userDataId = Optional.ofNullable(user.getUserData())
                .orElseThrow(() -> new UserNotCompletedRegistrationException(username))
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
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Set<Location> locations = new HashSet<>(locationRepository.findAllById(updateUserDataDto.getLocations()));
        if (locations.isEmpty()) throw new NoEntityWithSuchIdException("Location", updateUserDataDto.getLocations());
        UserData oldUserData = userDataRepository.findById(updateUserDataDto.getId())
                .orElseThrow(() -> new NoEntityWithSuchIdException("UserData", updateUserDataDto.getId()));
        if (!oldUserData.getOwnerUser().getId().equals(user.getId())) {
            // TODO здесь должно быть исключение кастомное + дописать его обработчик
        }
        UserData newUserData = UserDataMapper.toEntityUserData(updateUserDataDto, locations, oldUserData.getCreatedAt());
        newUserData.setOwnerUser(oldUserData.getOwnerUser());
        UserData savedUserData = userDataRepository.save(newUserData);
        return UserDataMapper.toUserDataDto(savedUserData);
    }

    public UserDataDto getUserDataByUser(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        UserData userData = Optional.ofNullable(user.getUserData())
                .orElseThrow(() -> new UserNotCompletedRegistrationException(username));
        return UserDataMapper.toUserDataDto(userData);
    }

    @Transactional
    public void deleteUserDataByUser(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        UserData userData = Optional.ofNullable(user.getUserData())
                .orElseThrow(() -> new UserNotCompletedRegistrationException(username));
        userDataRepository.delete(userData);
    }
}
