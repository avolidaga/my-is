package ru.se.ifmo.tinder.mapper;

import ru.se.ifmo.tinder.dto.location.CreateLocationDto;
import ru.se.ifmo.tinder.dto.location.LocationDto;
import ru.se.ifmo.tinder.model.Location;
public class LocationMapper {
    public static Location toEntityLocation(CreateLocationDto dto) {
        return Location.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .temperature(dto.getTemperature())
                .build();
    }

    public static Location toEntityLocation(LocationDto dto, Location oldLocation) {
        return Location.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .temperature(dto.getTemperature())
                .userData(oldLocation.getUserData())
                .build();
    }

    public static LocationDto toDtoLocation(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .temperature(location.getTemperature())
                .description(location.getDescription())
                .name(location.getName())
                .build();
    }
}
