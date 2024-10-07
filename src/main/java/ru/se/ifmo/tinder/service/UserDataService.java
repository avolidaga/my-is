package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.UserDataDto;
import ru.se.ifmo.tinder.mapper.UserDataMapper;
import ru.se.ifmo.tinder.model.Location;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.repository.LocationRepository;
import ru.se.ifmo.tinder.repository.UserDataRepository;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;
import ru.se.ifmo.tinder.service.exceptions.UserNotFoundException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDataService {
    private final UserDataRepository userDataRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;


    //TODO потенциальное место для транзакции
    public Integer insertUserData(UserDataDto userDataDto, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        UserData userData = UserDataMapper.toEntityUserData(userDataDto);

        UserData insertedId = userDataRepository.save(userData);

        user.setUser_data_id(userData);
        userRepository.save(user);

        return insertedId.getId();
    }


    public List<UserData> getAllUsersData(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Integer userId = user.getUser_data_id().getId();
        return userDataRepository.findAllUserDataExcludingUserId(userId);
    }

    public Optional<UserData> getCurrUserData(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Integer userId = user.getUser_data_id().getId();
        return userDataRepository.findById(userId);
    }

    public List<UserData> getUsersByPlanetId(Integer planetId) {
        Location location = locationRepository.findById(planetId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Location", planetId));
        return userDataRepository.findAllUserDataByPlanetId(location);
    }
}







