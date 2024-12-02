package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.LocationDto;
import ru.se.info.tinder.dto.RequestLocationDto;
import ru.se.info.tinder.mapper.LocationMapper;
import ru.se.info.tinder.model.Location;
import ru.se.info.tinder.repository.LocationRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

@RequiredArgsConstructor
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public Mono<LocationDto> createLocation(RequestLocationDto dto) {
        return locationRepository
                .save(LocationMapper.toEntityLocation(dto))
                .map(LocationMapper::toDtoLocation);
    }

    public Mono<LocationDto> updateLocationById(Long id, RequestLocationDto dto) {
        return locationRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Location", id)))
                .flatMap(existingLocation -> {
                    Location updatedLocation = LocationMapper.toEntityLocation(dto, existingLocation);
                    return locationRepository.save(updatedLocation);
                })
                .map(LocationMapper::toDtoLocation);
    }

    public Mono<Void> deleteLocationById(Long locationId) {
        return locationRepository.deleteById(locationId);
    }

    public Mono<LocationDto> getLocationDtoById(Long id) {
        return locationRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Location", id)))
                .map(LocationMapper::toDtoLocation);
    }

    public Flux<LocationDto> getAllLocations() {
        return locationRepository.findAll()
                .map(LocationMapper::toDtoLocation);
    }

    protected Mono<Location> getLocationById(Long id) {
        return locationRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Location", id)));
    }

    protected Flux<Location> getLocationsByIds(Flux<Long> ids) {
        return ids.flatMap(this::getLocationById);
    }

    public Flux<LocationDto> getLocationsListByIds(Flux<Long> locationIds) {
        return getLocationsByIds(locationIds)
                .map(LocationMapper::toDtoLocation);
    }
}
