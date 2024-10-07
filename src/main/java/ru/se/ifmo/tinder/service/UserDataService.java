package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.enums.Sex;
import ru.se.ifmo.tinder.repository.UserDataRepository;
import ru.se.ifmo.tinder.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDataService {
    private final UserDataRepository userDataRepository;
    private final UserRepository userRepository;


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



    public List<UserData> getAllUsersData(Principal principal) {
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);
        Integer userId = user.get().getUser_data_id().getId();
        return userDataRepository.findAllUserDataExcludingUserId(userId);
    }

    public Optional<UserData> getCurrUserData(Principal principal) {
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);
        Integer userId = user.get().getUser_data_id().getId();
        return userDataRepository.findById(userId);
    }

    public List<UserData> getUsersByPlanetId(String planetId) {
        // TODO реализовать логику отбора
        return null;
    }
}







