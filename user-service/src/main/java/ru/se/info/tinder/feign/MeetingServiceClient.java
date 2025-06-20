package ru.se.info.tinder.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.se.info.tinder.dto.InfoDto;

import javax.naming.ServiceUnavailableException;
import java.util.List;

@CircuitBreaker(name = "meeting-service")
@FeignClient(name = "meeting-service", fallback = MeetingServiceClient.Fallback.class)
public interface MeetingServiceClient {
    @GetMapping("/api/v1/locations/list")
    List<InfoDto> getLocationsByIds(@NotNull @RequestParam List<Long> locationIds,
                                    @RequestHeader("Authorization") String token);

    @Component
    @Log4j2
    class Fallback implements MeetingServiceClient {
        @Override
        @SneakyThrows
        public List<InfoDto> getLocationsByIds(List<Long> locationIds, String token) {
            log.warn("Circuit Breaker encountered an error while locations getting");
            throw new ServiceUnavailableException("Location-service unavailable");
        }
    }
}
