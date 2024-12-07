package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserDataService {
    private final UserDataRepository userDataRepository;
    private final LocationClient locationService;

    @Transactional
    public UserDataDto createUserData(CreateUserDataDto createUserDataDto, String token) {
        Set<Location> locations = new HashSet<>(locationService.getLocationsByIds(createUserDataDto.getLocations(), token)
                .getBody().stream()
                .map(l -> Location.builder().id(l.getId()).build())
                .collect(Collectors.toList()));

        UserData userData = UserDataMapper.toEntityUserData(createUserDataDto, locations);
        UserData savedUserData = userDataRepository.save(userData);

        return UserDataMapper.toUserDataDto(savedUserData);
    }

    public Page<UserDataDto> getAllUsersData(Pageable pageable) {
        return userDataRepository.findAll(pageable)
                .map(UserDataMapper::toUserDataDto);
    }

    public Page<UserDataDto> getUsersDataByLocationId(Long locationId, Pageable pageable) {
        return userDataRepository.findAllUserDataByLocationsId(locationId, pageable)
                .map(UserDataMapper::toUserDataDto);
    }

    @Transactional
    public UserDataDto updateUserDataById(UpdateUserDataDto updateUserDataDto, Long id, Principal principal, String token) {
        Set<Location> locations = new HashSet<>(locationService.getLocationsByIds(updateUserDataDto.getLocations(), token)
                .getBody().stream()
                .map(l -> Location.builder().id(l.getId()).build())
                .collect(Collectors.toList()));
        UserData oldUserData = userDataRepository.findById(id)
                .orElseThrow(() -> new NoEntityWithSuchIdException("UserData", id));
        if (!oldUserData.getOwnerUser().getUsername().equals(principal.getName())) {
            throw new IllegalArgumentException("User don't have enough rights for data updating");
        }
        UserData newUserData = UserDataMapper.toEntityUserData(updateUserDataDto, locations, oldUserData);
        UserData savedUserData = userDataRepository.save(newUserData);
        return UserDataMapper.toUserDataDto(savedUserData);
    }

    public UserDataDto getUserDataDtoById(Long id) {
        return UserDataMapper.toUserDataDto(getUserDataById(id));
    }

    @Transactional
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
}
