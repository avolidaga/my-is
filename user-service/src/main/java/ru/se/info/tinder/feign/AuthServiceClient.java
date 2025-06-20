package ru.se.info.tinder.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.ResponseUserDto;

import javax.naming.ServiceUnavailableException;

@CircuitBreaker(name = "auth-service-cb")
@FeignClient(name = "auth-service", fallback = AuthServiceClient.Fallback.class)
public interface AuthServiceClient {
    @PostMapping("/api/v1/user-management/validation")
    ResponseUserDto validateToken(@RequestBody String token);

    @Component
    @Log4j2
    class Fallback implements AuthServiceClient {
        @Override
        @SneakyThrows
        public ResponseUserDto validateToken(String token) {
            log.warn("Circuit Breaker encountered an error while token validation");
            throw new ServiceUnavailableException("Auth-service unavailable");
        }
    }
}
