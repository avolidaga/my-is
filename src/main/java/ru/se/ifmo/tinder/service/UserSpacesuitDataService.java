package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.repository.FabricTextureRepository;
import ru.se.ifmo.tinder.repository.RequestRepository;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.repository.UserSpacesuitDataRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSpacesuitDataService {
    // Пагинация
    private final UserSpacesuitDataRepository userSpacesuitDataRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final FabricTextureRepository fabricTextureRepository;

    public Integer insertUserSpacesuitData(Integer head, Integer chest, Integer waist, Integer hips, Integer footSize, Integer height, Integer fabricTextureId, Principal principal) {
        String username = principal.getName();

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            FabricTexture fabricTexture = fabricTextureRepository.findById(fabricTextureId).orElseThrow(() -> new IllegalArgumentException("Fabric texture not found"));
            UserSpacesuitData userSpacesuitData = UserSpacesuitData.builder()
                    .head(head)
                    .chest(chest)
                    .waist(waist)
                    .hips(hips)
                    .foot_size(footSize)
                    .height(height)
                    .fabricTextureId(fabricTexture)
                    .build();

            UserSpacesuitData userSpacesuit = userSpacesuitDataRepository.save(userSpacesuitData);

            User user = userOptional.get();
            user.setUserSpacesuitDataId(userSpacesuit);
            userRepository.save(user);
            UserRequest userRequest = UserRequest.builder()
                    .userSpacesuitDataId(userSpacesuit)
                    .build();

            requestRepository.save(userRequest);

            return userSpacesuit.getId();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // Измененный метод для получения данных о скафандре текущего пользователя с пагинацией
    public Page<UserSpacesuitData> getCurrUserSpacesuitData(Principal principal, Pageable pageable) {
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            Integer userId = userOptional.get().getUserSpacesuitDataId().getId();
            // Получаем идентификаторы скафандров
            Page<Integer> idPage = userSpacesuitDataRepository.getCurrUserSpacesuitData(userId, pageable);
            // Получаем список скафандров по их идентификаторам
            return userSpacesuitDataRepository.getListAllByUserSpacesuitDataIdIn(idPage.getContent(), pageable);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
