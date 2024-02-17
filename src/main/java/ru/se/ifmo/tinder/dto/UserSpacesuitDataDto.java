package ru.se.ifmo.tinder.dto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.model.enums.Status;


@Data
@Builder
public class UserSpacesuitDataDto {
    private Integer head;
    private Integer chest;
    private Integer waist;
    private Integer hips;
    private Integer foot_size;
    private Integer height;
    private Integer fabric_texture_id;

}



