package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public UserDataDto createUserData(CreateUserDataDto createUserDataDto, String token, Principal principal) {
        List<Location> locations = locationService.getLocationsByIds(createUserDataDto.getLocations(), token).stream()
                .map(l -> Location.builder().id(l.getId()).build())
                .collect(Collectors.toList());
        if (userDataRepository.findUserDataByUsername(principal.getName()).isPresent()) {
            throw new IllegalArgumentException("User have already complete registration");
        }
        UserData userData = UserDataMapper.toEntityUserData(createUserDataDto, new HashSet<>(locations));
        userData = userDataRepository.save(userData);
        return UserDataMapper.toUserDataDto(userData);
    }

    public List<UserDataDto> getAllUsersData() {
        return userDataRepository.findAll().stream().map(UserDataMapper::toUserDataDto).toList();
    }

    public List<UserDataDto> getUsersDataByLocationId(Long locationId) {
        return userDataRepository.findAllUserDataByLocationsId(locationId).stream().map(UserDataMapper::toUserDataDto).toList();
    }

    public UserDataDto updateUserDataById(UpdateUserDataDto updateUserDataDto, Long id, Principal principal, String token) {
        List<Location> locations = locationService.getLocationsByIds(updateUserDataDto.getLocations(), token).stream()
                .map(l -> Location.builder().id(l.getId()).build())
                .collect(Collectors.toList());

        UserData oldUserData = userDataRepository.findById(id)
                .orElseThrow(() -> new NoEntityWithSuchIdException("UserData", id));

        if (!oldUserData.getOwnerUser().getUsername().equals(principal.getName())) {
            throw new IllegalArgumentException("User don't have enough rights for data updating");
        }
        UserData newUserData = UserDataMapper.toEntityUserData(updateUserDataDto,
                new HashSet<>(locations), oldUserData);

        UserData savedUserData = userDataRepository.save(newUserData);
        return UserDataMapper.toUserDataDto(savedUserData);
    }

    public UserDataDto getUserDataDtoById(Long id) {
        return UserDataMapper.toUserDataDto(getUserDataById(id));
    }

    public void deleteUserDataById(Long id) {
        userDataRepository.deleteById(id);
    }

    protected UserData getUserDataById(Long userDataId) {
        return userDataRepository.findById(userDataId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("UserData", userDataId));
    }

    protected UserData getUserDataByUsername(String username) {
        return userDataRepository.findUserDataByUsername(username)
                .orElseThrow(() -> new UserNotCompletedRegistrationException(username));
    }

    protected void addProfileImageToUserData(Long userDataId, String id) {
        UserData userData = getUserDataById(userDataId);
        userData.setProfileImageId(id);
        userDataRepository.save(userData);
    }

    public void deleteProfileImageToUserData(Long userDataId) {
        UserData userData = getUserDataById(userDataId);
        userData.setProfileImageId(null);
        userDataRepository.save(userData);
    }
}
