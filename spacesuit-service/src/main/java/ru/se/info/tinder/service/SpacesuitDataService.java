package ru.se.info.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.*;
import ru.se.info.tinder.feign.FabricTextureClient;
import ru.se.info.tinder.mapper.FabricTextureMapper;
import ru.se.info.tinder.mapper.SpacesuitDataMapper;
import ru.se.info.tinder.mapper.UserRequestMapper;
import ru.se.info.tinder.model.FabricTexture;
import ru.se.info.tinder.model.UserRequest;
import ru.se.info.tinder.model.SpacesuitData;
import ru.se.info.tinder.repository.SpacesuitDataRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SpacesuitDataService {
    private final SpacesuitDataRepository spacesuitDataRepository;
    private final UserRequestService userRequestService;
    private final FabricTextureClient fabricTextureService;

    @Transactional
    public UserRequestDto createSpacesuitData(CreateSpacesuitDataDto createSpacesuitDataDto, String token) {
        Long fabricTextureId = createSpacesuitDataDto.getFabricTextureId();
        FabricTexture fabricTexture = FabricTextureMapper.toEntityFabricTexture(
                Optional.ofNullable(fabricTextureService
                                .getFabricTextureById(fabricTextureId, token).getBody())
                        .orElseThrow(() -> new NoEntityWithSuchIdException("Fabric texture", fabricTextureId)));

        SpacesuitData spacesuitData = SpacesuitDataMapper
                .toEntitySpacesuitData(createSpacesuitDataDto, fabricTexture);
        SpacesuitData savedSpacesuitData = spacesuitDataRepository.save(spacesuitData);

        UserRequest userRequest = userRequestService.createUserRequest(savedSpacesuitData);
        return UserRequestMapper.toUserRequestDto(userRequest);
    }

    public Page<SpacesuitDataDto> getCurrentUserSpacesuitData(Pageable pageable, Principal principal) {
        Page<SpacesuitData> userSpacesuitDataPage = spacesuitDataRepository
                .findAllUserSpacesuitDataByOwnerUserUsername(principal.getName(), pageable);
        return userSpacesuitDataPage.map(SpacesuitDataMapper::toSpacesuitDataDto);
    }

    public void deleteSpacesuitData(Long spacesuitDataId, Principal principal) {
        SpacesuitData spacesuitData = spacesuitDataRepository.findById(spacesuitDataId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Spacesuit data", spacesuitDataId));
        if (spacesuitData.getOwnerUser().getUsername().equals(principal.getName())) {
            throw new IllegalArgumentException("User don't have enough rights for data deleting");
        }
        spacesuitDataRepository.delete(spacesuitData);
    }

    public SpacesuitDataDto updateSpacesuitData(UpdateSpacesuitDataDto spacesuitDataDto, Principal principal, String token) {

        SpacesuitData oldSpacesuitData = spacesuitDataRepository.findById(spacesuitDataDto.getId())
                .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", spacesuitDataDto.getId()));
        if (oldSpacesuitData.getOwnerUser().getUsername().equals(principal.getName())) {
            throw new IllegalArgumentException("User don't have enough rights for data updating");
        }
        Long fabricTextureId = spacesuitDataDto.getFabricTextureId();
        FabricTexture fabricTexture = FabricTextureMapper.toEntityFabricTexture(
                Optional.ofNullable(fabricTextureService
                                .getFabricTextureById(fabricTextureId, token).getBody())
                        .orElseThrow(() -> new NoEntityWithSuchIdException("Fabric texture", fabricTextureId)));

        SpacesuitData newSpacesuitData = SpacesuitDataMapper.toEntitySpacesuitData(spacesuitDataDto, oldSpacesuitData, fabricTexture);
        SpacesuitData savedSpacesuitData = spacesuitDataRepository.save(newSpacesuitData);
        return SpacesuitDataMapper.toSpacesuitDataDto(savedSpacesuitData);
    }

    public SpacesuitDataDto getSpacesuitData(Long spacesuitDataId, Principal principal) {

        SpacesuitData spacesuitData = spacesuitDataRepository.findById(spacesuitDataId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", spacesuitDataId));
        if (spacesuitData.getOwnerUser().getUsername().equals(principal.getName())) {
            throw new IllegalArgumentException("User don't have enough rights for data getting");
        }
        return SpacesuitDataMapper.toSpacesuitDataDto(spacesuitData);
    }
}
