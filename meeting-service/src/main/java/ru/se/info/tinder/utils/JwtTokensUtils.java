package ru.se.info.tinder.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.ResponseUserDto;
import ru.se.info.tinder.feign.InfoServiceClient;

@Component
@RequiredArgsConstructor
public class JwtTokensUtils {

    private final InfoServiceClient infoServiceClient;

    public Mono<ResponseUserDto> check(String token) {
        return infoServiceClient.validateToken(token);
    }

}
