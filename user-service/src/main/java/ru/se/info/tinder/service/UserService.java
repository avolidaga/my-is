package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.CreateUserDto;
import ru.se.info.tinder.dto.UpdateUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.feign.InfoServiceClient;
import ru.se.info.tinder.mapper.UserDataMapper;
import ru.se.info.tinder.model.Location;
import ru.se.info.tinder.model.UserData;
import ru.se.info.tinder.repository.UserRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;
import ru.se.info.tinder.service.exception.UserNotCompletedRegistrationException;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final InfoServiceClient locationService;

    public UserDto createUserData(CreateUserDto createUserDto, String token, Principal principal) {
        List<Location> locations = locationService.getLocationsByIds(createUserDto.getLocations(), token).stream()
                .map(l -> Location.builder().id(l.getId()).build())
                .collect(Collectors.toList());
        if (userRepository.findUserDataByUsername(principal.getName()).isPresent()) {
            throw new IllegalArgumentException("User have already complete registration");
        }
        UserData userData = UserDataMapper.toEntityUserData(createUserDto, new HashSet<>(locations));
        userData = userRepository.save(userData);
        return UserDataMapper.toUserDataDto(userData);
    }

    public List<UserDto> getAllUsersData() {
        return userRepository.findAll().stream().map(UserDataMapper::toUserDataDto).toList();
    }

    public List<UserDto> getUsersDataByLocationId(Long locationId) {
        return userRepository.findAllUserDataByLocationsId(locationId).stream().map(UserDataMapper::toUserDataDto).toList();
    }

    public UserDto updateUserDataById(UpdateUserDto updateUserDto, Long id, Principal principal, String token) {
        List<Location> locations = locationService.getLocationsByIds(updateUserDto.getLocations(), token).stream()
                .map(l -> Location.builder().id(l.getId()).build())
                .collect(Collectors.toList());

        UserData oldUserData = userRepository.findById(id)
                .orElseThrow(() -> new NoEntityWithSuchIdException("UserData", id));

        if (!oldUserData.getOwnerUser().getUsername().equals(principal.getName())) {
            throw new IllegalArgumentException("User don't have enough rights for data updating");
        }
        UserData newUserData = UserDataMapper.toEntityUserData(updateUserDto,
                new HashSet<>(locations), oldUserData);

        UserData savedUserData = userRepository.save(newUserData);
        return UserDataMapper.toUserDataDto(savedUserData);
    }

    public UserDto getUserDataDtoById(Long id) {
        return UserDataMapper.toUserDataDto(getUserDataById(id));
    }

    public void deleteUserDataById(Long id) {
        userRepository.deleteById(id);
    }

    protected UserData getUserDataById(Long userDataId) {
        return userRepository.findById(userDataId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("UserData", userDataId));
    }

    protected UserData getUserDataByUsername(String username) {
        return userRepository.findUserDataByUsername(username)
                .orElseThrow(() -> new UserNotCompletedRegistrationException(username));
    }

    protected void addProfileImageToUserData(Long userDataId, String id) {
        UserData userData = getUserDataById(userDataId);
        userData.setProfileImageId(id);
        userRepository.save(userData);
    }

    public void deleteProfileImageToUserData(Long userDataId) {
        UserData userData = getUserDataById(userDataId);
        userData.setProfileImageId(null);
        userRepository.save(userData);
    }
}
