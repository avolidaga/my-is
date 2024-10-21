package ru.se.ifmo.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.spacesuit_data.CreateSpacesuitDataDto;
import ru.se.ifmo.tinder.dto.spacesuit_data.UserSpacesuitDataDto;
import ru.se.ifmo.tinder.dto.user_request.UserRequestDto;
import ru.se.ifmo.tinder.mapper.SpacesuitDataMapper;
import ru.se.ifmo.tinder.mapper.UserRequestMapper;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.model.enums.RequestStatus;
import ru.se.ifmo.tinder.repository.FabricTextureRepository;
import ru.se.ifmo.tinder.repository.UserRequestRepository;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.repository.UserSpacesuitDataRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;
import ru.se.ifmo.tinder.service.exceptions.UserNotFoundException;

import java.security.Principal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserSpacesuitDataService {
    private final UserSpacesuitDataRepository userSpacesuitDataRepository;
    private final UserRepository userRepository;
    private final UserRequestRepository userRequestRepository;
    private final FabricTextureRepository fabricTextureRepository;

    @Transactional
    public UserRequestDto createUserSpacesuitData(CreateSpacesuitDataDto createSpacesuitDataDto, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Long fabricTextureId = createSpacesuitDataDto.getFabricTextureId();
        FabricTexture fabricTexture = fabricTextureRepository.findById(fabricTextureId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Fabric texture", fabricTextureId));

        UserSpacesuitData userSpacesuitData = SpacesuitDataMapper
                .toEntitySpacesuitData(createSpacesuitDataDto, fabricTexture);

        UserSpacesuitData savedUserSpacesuitData = userSpacesuitDataRepository.save(userSpacesuitData);

        user.getUserSpacesuitDataSet().add(savedUserSpacesuitData); // TODO возможно нужно написать отдельный метод добавления в set в самой сущности
        userRepository.save(user);
        UserRequest userRequest = UserRequest.builder()
                .userSpacesuitData(savedUserSpacesuitData)
                .status(RequestStatus.NEW)
                .createdAt(LocalDateTime.now())
                .build();

        UserRequest savedUserRequest = userRequestRepository.save(userRequest);
        return UserRequestMapper.toUserRequestDto(savedUserRequest);
    }

    public Page<UserSpacesuitDataDto> getCurrentUserSpacesuitData(Principal principal, Pageable pageable) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Page<UserSpacesuitData> userSpacesuitDataPage = userSpacesuitDataRepository.findAllUserSpacesuitDataByOwnerUserId(user.getId(), pageable);
        return userSpacesuitDataPage.map(SpacesuitDataMapper::toSpacesuitDataDto);
    }
}
