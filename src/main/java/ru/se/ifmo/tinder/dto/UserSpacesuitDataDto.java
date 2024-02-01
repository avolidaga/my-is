package ru.se.ifmo.tinder.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserSpacesuitDataDto {
    private Integer head;
    private Integer chest;
    private Integer waist;
    private Integer hips;
    private Integer foot_size;
    private Integer height;
    private Integer fabricTextureDto;

}
