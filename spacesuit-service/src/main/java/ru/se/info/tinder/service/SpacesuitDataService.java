package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.*;
import ru.se.info.tinder.feign.FabricTextureClient;
import ru.se.info.tinder.mapper.FabricTextureMapper;
import ru.se.info.tinder.mapper.SpacesuitDataMapper;
import ru.se.info.tinder.mapper.UserRequestMapper;
import ru.se.info.tinder.model.SpacesuitData;
import ru.se.info.tinder.repository.SpacesuitDataRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import javax.transaction.Transactional;
import java.security.Principal;

@RequiredArgsConstructor
@Service
public class SpacesuitDataService {
    private final SpacesuitDataRepository spacesuitDataRepository;
    private final UserRequestService userRequestService;
    private final FabricTextureClient fabricTextureService;

    public Mono<UserRequestDto> createSpacesuitData(CreateSpacesuitDataDto createSpacesuitDataDto, String token) {

        return fabricTextureService.getFabricTextureById(createSpacesuitDataDto.getFabricTextureId(), token)
                .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Fabric texture", createSpacesuitDataDto.getFabricTextureId())))
                .map(FabricTextureMapper::toEntityFabricTexture)
                .flatMap(
                        (fabricTexture) -> {
                            SpacesuitData spacesuitData = SpacesuitDataMapper
                                    .toEntitySpacesuitData(createSpacesuitDataDto, fabricTexture);
                            return Mono.fromCallable(
                                    () -> spacesuitDataRepository.save(spacesuitData)
                            ).flatMap(
                                    (savedSpacesuitData) -> userRequestService.createUserRequest(savedSpacesuitData)
                                            .map(UserRequestMapper::toUserRequestDto)
                            );
                        }
                );
    }

    public Flux<SpacesuitDataDto> getCurrentUserSpacesuitData(Principal principal) {
        return Flux.fromStream(
                () -> spacesuitDataRepository.findAllUserSpacesuitDataByOwnerUserUsername(principal.getName()).stream()
        ).map(SpacesuitDataMapper::toSpacesuitDataDto);
    }

    public Mono<Object> deleteSpacesuitData(Long spacesuitDataId, Principal principal) {
        return Mono.fromCallable(
                () -> spacesuitDataRepository.findById(spacesuitDataId)
                        .orElseThrow(() -> new NoEntityWithSuchIdException("Spacesuit data", spacesuitDataId))
        ).flatMap(
                (spacesuitData) -> {
                    if (!spacesuitData.getOwnerUser().getUsername().equals(principal.getName())) {
                        return Mono.error(new IllegalArgumentException("User don't have enough rights for data deleting"));
                    }
                    return Mono.fromRunnable(() -> spacesuitDataRepository.delete(spacesuitData));
                }
        );
    }

    public Mono<SpacesuitDataDto> updateSpacesuitData(UpdateSpacesuitDataDto spacesuitDataDto, Principal principal, String token) {
        return Mono.fromCallable(
                () -> spacesuitDataRepository.findById(spacesuitDataDto.getId())
                        .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", spacesuitDataDto.getId()))
        ).flatMap(
                (oldSpacesuitData) -> {
                    if (!oldSpacesuitData.getOwnerUser().getUsername().equals(principal.getName())) {
                        throw new IllegalArgumentException("User don't have enough rights for data updating");
                    }
                    Long fabricTextureId = spacesuitDataDto.getFabricTextureId();

                    return fabricTextureService.getFabricTextureById(fabricTextureId, token)
                            .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Fabric texture", fabricTextureId)))
                            .map(FabricTextureMapper::toEntityFabricTexture)
                            .flatMap(
                                    (fabricTexture) -> {
                                        SpacesuitData newSpacesuitData = SpacesuitDataMapper.toEntitySpacesuitData(spacesuitDataDto, oldSpacesuitData, fabricTexture);
                                        return Mono.fromCallable(
                                                () -> spacesuitDataRepository.save(newSpacesuitData)
                                        ).map(SpacesuitDataMapper::toSpacesuitDataDto);
                                    }
                            );
                }
        );
    }

    public Mono<SpacesuitDataDto> getSpacesuitData(Long spacesuitDataId, Principal principal) {

        return Mono.fromCallable(
                () -> spacesuitDataRepository.findById(spacesuitDataId)
                        .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", spacesuitDataId))
        ).flatMap(
                (spacesuitData) -> {
                    if (!spacesuitData.getOwnerUser().getUsername().equals(principal.getName())) {
                        return Mono.error(new IllegalArgumentException("User don't have enough rights for data getting"));
                    }
                    return Mono.just(spacesuitData).map(SpacesuitDataMapper::toSpacesuitDataDto);
                }
        );
    }
}
