package ru.se.info.tinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebSocketImageResponse {
    private boolean isSuccess;
    private String payload;
}
