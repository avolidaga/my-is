package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.FabricTextureDto;
import ru.se.info.tinder.dto.RequestFabricTextureDto;
import ru.se.info.tinder.mapper.FabricTextureMapper;
import ru.se.info.tinder.model.FabricTexture;
import ru.se.info.tinder.repository.FabricTextureRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

@RequiredArgsConstructor
@Service
public class FabricTextureService {

    private final FabricTextureRepository fabricTextureRepository;

    public Mono<FabricTextureDto> createFabricTexture(RequestFabricTextureDto dto) {
        return fabricTextureRepository
                .save(FabricTextureMapper.toEntityFabricTexture(dto))
                .map(FabricTextureMapper::toDtoFabricTexture);
    }

    public Flux<FabricTextureDto> getAllFabricTextures() {
        return fabricTextureRepository
                .findAll()
                .map(FabricTextureMapper::toDtoFabricTexture);
    }

    public Mono<Void> deleteFabricTextureById(Long fabricTextureId) {
        return fabricTextureRepository
                .findById(fabricTextureId)
                .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Fabric texture", fabricTextureId)))
                .flatMap(fabricTexture -> fabricTextureRepository.deleteById(fabricTexture.getId()));
    }

    public Mono<FabricTextureDto> updateFabricTextureById(Long fabricTextureId, RequestFabricTextureDto dto) {
        return fabricTextureRepository
                .findById(fabricTextureId)
                .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Fabric texture", fabricTextureId)))
                .flatMap(existingFabricTexture -> {
                    FabricTexture updatedFabricTexture = FabricTextureMapper.toEntityFabricTexture(fabricTextureId, dto);
                    return fabricTextureRepository.save(updatedFabricTexture);
                })
                .map(FabricTextureMapper::toDtoFabricTexture);
    }

    public Mono<FabricTextureDto> getFabricTextureById(Long id) {
        return fabricTextureRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Fabric texture", id)))
                .map(FabricTextureMapper::toDtoFabricTexture);
    }
}
