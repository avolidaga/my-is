package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.fabric_texture.RequestFabricTextureDto;
import ru.se.ifmo.tinder.dto.fabric_texture.FabricTextureDto;
import ru.se.ifmo.tinder.mapper.FabricTextureMapper;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.repository.FabricTextureRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;

@RequiredArgsConstructor
@Service
public class FabricTextureService {

    private final FabricTextureRepository fabricTextureRepository;

    public FabricTextureDto createFabricTexture(RequestFabricTextureDto dto) {
        FabricTexture fabricTexture = fabricTextureRepository.save(FabricTextureMapper.toEntityFabricTexture(dto));
        return FabricTextureMapper.toDtoFabricTexture(fabricTexture);
    }

    public Page<FabricTextureDto> getAllFabricTexture(Pageable pageable) {
        return fabricTextureRepository.findAll(pageable).map(FabricTextureMapper::toDtoFabricTexture);
    }

    public void deleteLocationById(Long fabricTextureId) {
        fabricTextureRepository.deleteById(fabricTextureId);
    }

    public FabricTextureDto updateFabricTextureById(Long fabricTextureId, RequestFabricTextureDto dto) {
        fabricTextureRepository.findById(fabricTextureId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Fabric texture", fabricTextureId));
        FabricTexture newFabricTexture = FabricTextureMapper.toEntityFabricTexture(fabricTextureId, dto);
        fabricTextureRepository.save(newFabricTexture);
        return FabricTextureMapper.toDtoFabricTexture(newFabricTexture);
    }

    public FabricTextureDto getFabricTextureDtoById(Long id) {
        FabricTexture fabricTexture = getFabricTextureById(id);
        return FabricTextureMapper.toDtoFabricTexture(fabricTexture);
    }

    protected FabricTexture getFabricTextureById(Long id) {
        return fabricTextureRepository.findById(id)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Fabric texture", id));
    }
}
