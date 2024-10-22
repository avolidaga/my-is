package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.location.RequestLocationDto;
import ru.se.ifmo.tinder.dto.location.LocationDto;
import ru.se.ifmo.tinder.mapper.LocationMapper;
import ru.se.ifmo.tinder.model.Location;
import ru.se.ifmo.tinder.repository.LocationRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationDto createLocation(RequestLocationDto dto) {
        Location savedLocation = locationRepository.save( LocationMapper.toEntityLocation(dto));
        return LocationMapper.toDtoLocation(savedLocation);
    }

    public LocationDto updateLocationById(Long id, RequestLocationDto dto) {
        Location oldLocation = locationRepository.findById(id)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Location", id));
        Location newLocation = LocationMapper.toEntityLocation(dto, oldLocation);
        locationRepository.save(newLocation);
        return LocationMapper.toDtoLocation(newLocation);
    }

    public void deleteLocationById(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    public LocationDto getLocationDtoById(Long id) {
        return LocationMapper.toDtoLocation(getLocationById(id));
    }

    public Page<LocationDto> getAllLocations(Pageable pageable) {
        return locationRepository.findAll(pageable).map(LocationMapper::toDtoLocation);
    }

    protected Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new NoEntityWithSuchIdException("Location", id));
    }

    protected List<Location> getLocationsByIds(List<Long> ids) {
        return ids.stream().map(this::getLocationById)
                .collect(Collectors.toList());
    }
}
