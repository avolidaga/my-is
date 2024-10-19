package ru.se.ifmo.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.UserSpacesuitDataDto;
import ru.se.ifmo.tinder.mapper.SpacesuitDataMapper;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.model.enums.RequestStatus;
import ru.se.ifmo.tinder.repository.FabricTextureRepository;
import ru.se.ifmo.tinder.repository.RequestRepository;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.repository.UserSpacesuitDataRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;
import ru.se.ifmo.tinder.service.exceptions.NoSpacesuitDataException;
import ru.se.ifmo.tinder.service.exceptions.UserNotFoundException;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSpacesuitDataService {
    // Пагинация
    private final UserSpacesuitDataRepository userSpacesuitDataRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final FabricTextureRepository fabricTextureRepository;

    @Transactional
    public Integer insertUserSpacesuitData(UserSpacesuitDataDto userSpacesuitDataDto, Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Integer fabricTextureId = userSpacesuitDataDto.getFabric_texture_id();
        FabricTexture fabricTexture = fabricTextureRepository.findById(fabricTextureId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Fabric texture", fabricTextureId));
        UserSpacesuitData userSpacesuitData = SpacesuitDataMapper
                .toEntitySpacesuitData(userSpacesuitDataDto, fabricTexture);

        UserSpacesuitData userSpacesuit = userSpacesuitDataRepository.save(userSpacesuitData);

        user.setUserSpacesuitDataId(userSpacesuit);
        userRepository.save(user);
        UserRequest userRequest = UserRequest.builder()
                .userSpacesuitDataId(userSpacesuit)
                .status(RequestStatus.NEW)
                .build();

        requestRepository.save(userRequest);
        return userSpacesuit.getId();
    }


    public Page<UserSpacesuitData> getCurrUserSpacesuitData(Principal principal, Pageable pageable) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Integer userId = Optional.ofNullable(user.getUserSpacesuitDataId())
                .orElseThrow(() -> new NoSpacesuitDataException(username))
                .getId();
        Page<Integer> idPage = userRepository.getCurrUserSpacesuitData(userId, pageable);
        return userSpacesuitDataRepository.getListAllByUserSpacesuitDataIdIn(idPage.getContent(), pageable);
    }
}
