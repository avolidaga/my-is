package ru.se.info.tinder.mapper;

import ru.se.info.tinder.dto.LocationDto;
import ru.se.info.tinder.model.Location;

public class LocationMapper {
    public static Location toEntityLocation(LocationDto dto) {
        return Location.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .temperature(dto.getTemperature())
                .build();
    }
}
