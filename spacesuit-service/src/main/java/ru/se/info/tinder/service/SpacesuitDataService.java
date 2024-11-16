package ru.se.info.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.CreateSpacesuitDataDto;
import ru.se.info.tinder.dto.UpdateSpacesuitDataDto;
import ru.se.info.tinder.dto.SpacesuitDataDto;
import ru.se.info.tinder.dto.UserRequestDto;
import ru.se.info.tinder.feign.FabricTextureClient;
import ru.se.info.tinder.mapper.FabricTextureMapper;
import ru.se.info.tinder.mapper.SpacesuitDataMapper;
import ru.se.info.tinder.mapper.UserRequestMapper;
import ru.se.info.tinder.model.FabricTexture;
import ru.se.info.tinder.model.User;
import ru.se.info.tinder.model.UserRequest;
import ru.se.info.tinder.model.SpacesuitData;
import ru.se.info.tinder.repository.SpacesuitDataRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SpacesuitDataService {
    private final SpacesuitDataRepository spacesuitDataRepository;
    private final UserRequestService userRequestService;
    private final FabricTextureClient fabricTextureService;

    @Transactional
    public UserRequestDto createSpacesuitData(CreateSpacesuitDataDto createSpacesuitDataDto) {
        // User user = userService.getUserByUsername(principal.getName()); TODO брать из токена username или еще как-то - продумать аутентификацию
        User user = null; // пока как заглушка

        Long fabricTextureId = createSpacesuitDataDto.getFabricTextureId();
        FabricTexture fabricTexture = FabricTextureMapper.toEntityFabricTexture(
                Optional.ofNullable(fabricTextureService
                                .getFabricTextureById(fabricTextureId).getBody())
                        .orElseThrow(() -> new NoEntityWithSuchIdException("Fabric texture", fabricTextureId)));

        SpacesuitData spacesuitData = SpacesuitDataMapper
                .toEntitySpacesuitData(createSpacesuitDataDto, fabricTexture);
        spacesuitData.setOwnerUser(user);
        SpacesuitData savedSpacesuitData = spacesuitDataRepository.save(spacesuitData);

        UserRequest userRequest = userRequestService.createUserRequest(savedSpacesuitData);
        return UserRequestMapper.toUserRequestDto(userRequest);
    }

    public Page<SpacesuitDataDto> getCurrentUserSpacesuitData(Pageable pageable) {
        //User user = userService.getUserByUsername(principal.getName()); TODO брать из токена username или еще как-то - продумать аутентификацию
        User user = null; // пока как заглушка
        Page<SpacesuitData> userSpacesuitDataPage = spacesuitDataRepository
                .findAllUserSpacesuitDataByOwnerUserId(user.getId(), pageable);
        return userSpacesuitDataPage.map(SpacesuitDataMapper::toSpacesuitDataDto);
    }

    public void deleteSpacesuitData(Long spacesuitDataId) {
        // User user = userService.getUserByUsername(principal.getName()); TODO брать из токена username или еще как-то - продумать аутентификацию
        User user = null; // пока как заглушка
        SpacesuitData spacesuitData = spacesuitDataRepository.findById(spacesuitDataId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Spacesuit data", spacesuitDataId));
        if (spacesuitData.getOwnerUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User don't have enough rights for data deleting");
        }
        spacesuitDataRepository.delete(spacesuitData);
    }

    public SpacesuitDataDto updateSpacesuitData(UpdateSpacesuitDataDto spacesuitDataDto) {
        // User user = userService.getUserByUsername(principal.getName()); TODO брать из токена username или еще как-то - продумать аутентификацию
        User user = null; // пока как заглушка
        SpacesuitData oldSpacesuitData = spacesuitDataRepository.findById(spacesuitDataDto.getId())
                .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", spacesuitDataDto.getId()));
        if (oldSpacesuitData.getOwnerUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User don't have enough rights for data updating");
        }
        Long fabricTextureId = spacesuitDataDto.getFabricTextureId();
        FabricTexture fabricTexture = FabricTextureMapper.toEntityFabricTexture(
                Optional.ofNullable(fabricTextureService
                                .getFabricTextureById(fabricTextureId).getBody())
                        .orElseThrow(() -> new NoEntityWithSuchIdException("Fabric texture", fabricTextureId)));

        SpacesuitData newSpacesuitData = SpacesuitDataMapper.toEntitySpacesuitData(spacesuitDataDto, oldSpacesuitData, fabricTexture);
        SpacesuitData savedSpacesuitData = spacesuitDataRepository.save(newSpacesuitData);
        return SpacesuitDataMapper.toSpacesuitDataDto(savedSpacesuitData);
    }

    public SpacesuitDataDto getSpacesuitData(Long spacesuitDataId) {
        //User user = userService.getUserByUsername(principal.getName()); TODO брать из токена username или еще как-то - продумать аутентификацию
        User user = null; // пока как заглушка
        SpacesuitData spacesuitData = spacesuitDataRepository.findById(spacesuitDataId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", spacesuitDataId));
        if (spacesuitData.getOwnerUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User don't have enough rights for data getting");
        }
        return SpacesuitDataMapper.toSpacesuitDataDto(spacesuitData);
    }
}
