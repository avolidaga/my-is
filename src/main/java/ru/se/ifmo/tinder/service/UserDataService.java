package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.UserDataDto;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.enums.Location;
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

    public List<UserData> getMars(){
        List<Integer> idList = userDataRepository.getUserDataOnMars();
        return userDataRepository.getListAllByUserDataIdIn(idList);
    }
    public List<UserData> getEarth(){
        List<Integer> idList = userDataRepository.getUserDataOnEarth();
        return userDataRepository.getListAllByUserDataIdIn(idList);
    }
    public Integer insertUserData(LocalDate birthdate, Sex sex, Integer weight, Integer height, String hairColor, Location location, String firstname, Principal principal){
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);
        Integer userId = user.get().getId();
        return userDataRepository.insertUserData(birthdate,sex.toString(),weight,height,hairColor, location.toString(),firstname, userId);
    }
    public List<UserData> getAllUserData(Principal principal){
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);
        Integer userId = user.get().getUser_data_id().getId();
        List<Integer> idList = userDataRepository.getAllUserData(userId);
        return userDataRepository.getListAllByUserDataIdIn(idList);
    }

}







