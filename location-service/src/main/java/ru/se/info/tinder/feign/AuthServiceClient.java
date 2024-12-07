package ru.se.info.tinder.feign;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.ResponseUserDto;

@ReactiveFeignClient("auth-service")
public interface AuthServiceClient {
    @PostMapping("/api/v1/user-management/validation")
    Mono<ResponseUserDto> validateToken(@RequestBody String token);
}
