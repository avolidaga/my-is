package ru.se.ifmo.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.UserDataDto;
import ru.se.ifmo.tinder.mapper.UserDataMapper;
import ru.se.ifmo.tinder.model.Location;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.repository.FabricTextureRepository;
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
    private final FabricTextureRepository fabricTextureRepository;


    @Transactional
    public Integer insertUserData(UserDataDto userDataDto, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Set<Location> locations = new HashSet<>(locationRepository.findAllById(userDataDto.getLocation()));
        if (locations.isEmpty()) throw new NoEntityWithSuchIdException("Location", userDataDto.getLocation());
        UserData userData = UserDataMapper.toEntityUserData(userDataDto, locations);

        UserData insertedId = userDataRepository.save(userData);

        user.setUser_data_id(userData);
        userRepository.save(user);

        return insertedId.getId();
    }


    public Page<UserData> getAllUsersData(Principal principal, Pageable pageable) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Integer userId = Optional.ofNullable(user.getUser_data_id())
                .orElseThrow(() -> new UserNotCompletedRegistrationException(username))
                .getId();
        return userDataRepository.findAllUserDataExcludingUserId(userId, pageable);
    }

    // Метод для получения текущего пользователя
    public Optional<UserData> getCurrUserData(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return Optional.ofNullable(user.getUser_data_id());
    }

    public Page<UserData> getUsersByPlanetId(Integer planetId, Pageable pageable) {
        return userDataRepository.findUserDataByLocationsId(planetId, pageable);
    }

}
