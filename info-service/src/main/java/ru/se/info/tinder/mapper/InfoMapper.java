package ru.se.info.tinder.mapper;

import ru.se.info.tinder.dto.InfoDto;
import ru.se.info.tinder.dto.RequestInfoDto;
import ru.se.info.tinder.model.Info;

public class InfoMapper {
    public static Info toEntityInfo(RequestInfoDto dto) {
        return Info.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .temperature(dto.getTemperature())
                .build();
    }

    public static Info toEntityInfo(RequestInfoDto dto, Info oldInfo) {
        return Info.builder()
                .id(oldInfo.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .temperature(dto.getTemperature())
                .build();
    }

    public static InfoDto toDtoInfo(Info info) {
        return InfoDto.builder()
                .id(info.getId())
                .temperature(info.getTemperature())
                .description(info.getDescription())
                .name(info.getName())
                .build();
    }
}
