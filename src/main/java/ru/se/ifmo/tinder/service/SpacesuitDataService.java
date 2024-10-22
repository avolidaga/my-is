package ru.se.ifmo.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.spacesuit_data.CreateSpacesuitDataDto;
import ru.se.ifmo.tinder.dto.spacesuit_data.UpdateSpacesuitDataDto;
import ru.se.ifmo.tinder.dto.spacesuit_data.SpacesuitDataDto;
import ru.se.ifmo.tinder.dto.user_request.UserRequestDto;
import ru.se.ifmo.tinder.mapper.SpacesuitDataMapper;
import ru.se.ifmo.tinder.mapper.UserRequestMapper;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.SpacesuitData;
import ru.se.ifmo.tinder.repository.SpacesuitDataRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class SpacesuitDataService {
    private final SpacesuitDataRepository spacesuitDataRepository;
    private final UserService userService;
    private final UserRequestService userRequestService;
    private final FabricTextureService fabricTextureService;

    @Transactional
    public UserRequestDto createSpacesuitData(CreateSpacesuitDataDto createSpacesuitDataDto, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        Long fabricTextureId = createSpacesuitDataDto.getFabricTextureId();
        FabricTexture fabricTexture = fabricTextureService.getFabricTextureById(fabricTextureId);

        SpacesuitData spacesuitData = SpacesuitDataMapper
                .toEntitySpacesuitData(createSpacesuitDataDto, fabricTexture);
        spacesuitData.setOwnerUser(user);
        SpacesuitData savedSpacesuitData = spacesuitDataRepository.save(spacesuitData);

        UserRequest userRequest = userRequestService.createUserRequest(savedSpacesuitData);
        return UserRequestMapper.toUserRequestDto(userRequest);
    }

    public Page<SpacesuitDataDto> getCurrentUserSpacesuitData(Principal principal, Pageable pageable) {
        User user = userService.getUserByUsername(principal.getName());
        Page<SpacesuitData> userSpacesuitDataPage = spacesuitDataRepository
                .findAllUserSpacesuitDataByOwnerUserId(user.getId(), pageable);
        return userSpacesuitDataPage.map(SpacesuitDataMapper::toSpacesuitDataDto);
    }

    public void deleteSpacesuitData(Long spacesuitDataId, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        SpacesuitData spacesuitData = spacesuitDataRepository.findById(spacesuitDataId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Spacesuit data", spacesuitDataId));
        if (spacesuitData.getOwnerUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User don't have enough rights for data deleting");
        }
        spacesuitDataRepository.delete(spacesuitData);
    }

    public SpacesuitDataDto updateSpacesuitData(UpdateSpacesuitDataDto spacesuitDataDto, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        SpacesuitData oldSpacesuitData = spacesuitDataRepository.findById(spacesuitDataDto.getId())
                .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", spacesuitDataDto.getId()));
        if (oldSpacesuitData.getOwnerUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User don't have enough rights for data updating");
        }
        Long fabricTextureId = spacesuitDataDto.getFabricTextureId();
        FabricTexture fabricTexture = fabricTextureService.getFabricTextureById(fabricTextureId);
        SpacesuitData newSpacesuitData = SpacesuitDataMapper.toEntitySpacesuitData(spacesuitDataDto, oldSpacesuitData, fabricTexture);
        SpacesuitData savedSpacesuitData = spacesuitDataRepository.save(newSpacesuitData);
        return SpacesuitDataMapper.toSpacesuitDataDto(savedSpacesuitData);
    }

    public SpacesuitDataDto getSpacesuitData(Long spacesuitDataId, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        SpacesuitData spacesuitData = spacesuitDataRepository.findById(spacesuitDataId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", spacesuitDataId));
        if (spacesuitData.getOwnerUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User don't have enough rights for data getting");
        }
        return SpacesuitDataMapper.toSpacesuitDataDto(spacesuitData);
    }
}
