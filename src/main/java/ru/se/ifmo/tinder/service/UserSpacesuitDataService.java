package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.repository.UserSpacesuitDataRepository;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSpacesuitDataService {
    private final UserSpacesuitDataRepository userSpacesuitDataRepository;
    private final UserRepository userRepository;

    public Integer insertUserSpacesuitData(Integer head, Integer chest, Integer waist, Integer hips, Integer foot_size, Integer height, Integer fabric_texture_id,  Principal principal){
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);
        Integer userId = user.get().getId();
        return userSpacesuitDataRepository.insertUserSpacesuitData(head,chest,waist, hips, foot_size, height,fabric_texture_id,userId);
    }

}

