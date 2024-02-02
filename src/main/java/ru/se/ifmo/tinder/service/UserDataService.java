package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.UserDataDto;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.enums.Location;
import ru.se.ifmo.tinder.model.enums.Sex;
import ru.se.ifmo.tinder.repository.UserDataRepository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDataService {
    private final UserDataRepository userDataRepository;

    public List<UserData> getMars(){
        List<Integer> idList = userDataRepository.getUserDataOnMars();
        return userDataRepository.getListAllByUserDataIdIn(idList);
    }
    public List<UserData> getEarth(){
        List<Integer> idList = userDataRepository.getUserDataOnEarth();
        return userDataRepository.getListAllByUserDataIdIn(idList);
    }
    public Integer insertUserData(LocalDate birthdate, Sex sex,Integer weight, Integer height, String hairColor,Location location){
        return userDataRepository.insertUserData(birthdate,sex,weight,height,hairColor, location);
    }
}



