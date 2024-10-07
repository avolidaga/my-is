package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.enums.Sex;
import ru.se.ifmo.tinder.repository.UserDataRepository;
import ru.se.ifmo.tinder.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDataService {
    private final UserDataRepository userDataRepository;
    private final UserRepository userRepository;

    // Метод для вставки данных пользователя
    public Integer insertUserData(LocalDate birthdate, Sex sex, Integer weight, Integer height, String hairColor, String firstname, Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            UserData userData = UserData.builder()
                    .birthdate(birthdate)
                    .sex(sex)
                    .weight(weight)
                    .height(height)
                    .hairColor(hairColor)
                    .firstname(firstname)
                    .build();

            UserData insertedId = userDataRepository.save(userData);

            User user = userOptional.get();
            user.setUser_data_id(userData);
            userRepository.save(user);

            return insertedId.getId();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // Метод для получения всех пользователей с пагинацией
    public Page<UserData> getAllUsersData(Principal principal, Pageable pageable) {
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            Integer userId = user.get().getUser_data_id().getId();
            return userDataRepository.findAllUserDataExcludingUserId(userId, pageable);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // Метод для получения текущего пользователя
    public Optional<UserData> getCurrUserData(Principal principal) {
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            Integer userId = user.get().getUser_data_id().getId();
            return userDataRepository.findById(userId);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // Заглушка для пользователей по планете
    public Page<UserData> getUsersByPlanetId(String planetId, Pageable pageable) {
        // TODO: Реализовать фильтрацию пользователей по планете
        return null;
    }
}

