package ru.se.info.tinder.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import ru.se.info.tinder.dto.LocationDto;

import javax.naming.ServiceUnavailableException;
import java.util.List;

@CircuitBreaker(name = "location-service-cb")
@ReactiveFeignClient(name = "location-service", fallback = LocationClient.Fallback.class)
public interface LocationClient {
    @GetMapping("/api/v1/locations/list")
    Flux<LocationDto> getLocationsByIds(@NotNull @RequestParam List<Long> locationIds,
                                        @RequestHeader("Authorization") String token);

    @Component
    @Log4j2
    class Fallback implements LocationClient {
        @Override
        public Flux<LocationDto> getLocationsByIds(List<Long> locationIds, String token) {
            log.warn("Circuit Breaker encountered an error while locations getting");
            return Flux.error(new ServiceUnavailableException("Location-service unavailable"));
        }
    }
}
