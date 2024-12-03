package ru.se.info.tinder.feign;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient("auth-service")
public interface AuthServiceClient {
    @PostMapping("/api/v1/user-management/validation")
    Mono<SignedJWT> validateToken(@RequestBody String token);
}
