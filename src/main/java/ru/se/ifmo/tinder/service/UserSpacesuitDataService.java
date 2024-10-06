package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.repository.FabricTextureRepository;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.repository.UserSpacesuitDataRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSpacesuitDataService {
    private final UserSpacesuitDataRepository userSpacesuitDataRepository;
    private final UserRepository userRepository;

    private final FabricTextureRepository fabricTextureRepository;
    public Integer insertUserSpacesuitData(Integer head, Integer chest, Integer waist, Integer hips, Integer footSize, Integer height, Integer fabricTextureId, Principal principal) {
        String username = principal.getName();

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {

            FabricTexture fabricTexture = fabricTextureRepository.findById(fabricTextureId).get();
            UserSpacesuitData userSpacesuitData =  UserSpacesuitData.builder()
                    .head(head)
                    .chest(chest)
                    .waist(waist)
                    .hips(hips)
                    .foot_size(footSize)
                    .height(height)
                    .fabricTextureId(fabricTexture)
                    .build();

            UserSpacesuitData insertedId = userSpacesuitDataRepository.save(userSpacesuitData);

            User user = userOptional.get();
            user.setUserSpacesuitDataId(insertedId);
            userRepository.save(user);

            return insertedId.getId();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }



    public List<UserSpacesuitData> getCurrUserSpacesuitData(Principal principal){
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);
        Integer userId = user.get().getUserSpacesuitDataId().getId();
        List<Integer> idList = userSpacesuitDataRepository.getCurrUserSpacesuitData(userId);
        return userSpacesuitDataRepository.getListAllByUserSpacesuitDataIdIn(idList);
    }
}

