package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.UserDataDto;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.repository.UserDataRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDataService {
    private final UserDataRepository userDataRepository;

    public List<UserDataDto> getMars(){
        return userDataRepository.getUserDataOnMars();
    }
    public List<UserDataDto> getEarth(){
        return userDataRepository.getUserDataOnEarth();
    }
}
