package ru.se.info.tinder.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.ResponseInfoDto;
import ru.se.info.tinder.feign.AuthServiceClient;

@Component
@RequiredArgsConstructor
public class JwtTokensUtils {

    private final AuthServiceClient authServiceClient;

    public Mono<ResponseInfoDto> check(String token) {
        return authServiceClient.validateToken(token);
    }
}
