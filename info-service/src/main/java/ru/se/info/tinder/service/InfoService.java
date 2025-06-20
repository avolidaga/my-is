package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.InfoDto;
import ru.se.info.tinder.dto.RequestInfoDto;
import ru.se.info.tinder.mapper.InfoMapper;
import ru.se.info.tinder.model.Info;
import ru.se.info.tinder.repository.InfoRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

@RequiredArgsConstructor
@Service
public class InfoService {

    private final InfoRepository infoRepository;

    public Mono<InfoDto> createInfo(RequestInfoDto dto) {
        return infoRepository
                .save(InfoMapper.toEntityInfo(dto))
                .map(InfoMapper::toDtoInfo);
    }

    public Mono<InfoDto> updateInfoById(Long id, RequestInfoDto dto) {
        return infoRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Info", id)))
                .flatMap(existingInfo -> {
                    Info updatedInfo = InfoMapper.toEntityInfo(dto, existingInfo);
                    return infoRepository.save(updatedInfo);
                })
                .map(InfoMapper::toDtoInfo);
    }

    public Mono<Void> deleteInfoById(Long infoId) {
        return infoRepository.deleteById(infoId);
    }

    public Mono<InfoDto> getInfoDtoById(Long id) {
        return infoRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Info", id)))
                .map(InfoMapper::toDtoInfo);
    }

    public Flux<InfoDto> getAllInfos() {
        return infoRepository.findAll()
                .map(InfoMapper::toDtoInfo);
    }

    protected Mono<Info> getInfoById(Long id) {
        return infoRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Info", id)));
    }

    protected Flux<Info> getInfosByIds(Flux<Long> ids) {
        return ids.flatMap(this::getInfoById);
    }

    public Flux<InfoDto> getInfosListByIds(Flux<Long> infoIds) {
        return getInfosByIds(infoIds)
                .map(InfoMapper::toDtoInfo);
    }
}
