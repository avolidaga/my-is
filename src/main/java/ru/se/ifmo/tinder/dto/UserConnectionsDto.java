package ru.se.ifmo.tinder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class UserConnectionsDto {
    //private Integer user_id_1;
    private Integer user_id_2;
}
