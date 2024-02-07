package ru.se.ifmo.tinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.se.ifmo.tinder.model.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Integer user_spacesuit_data_id;

}
