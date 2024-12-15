package ru.se.info.tinder.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.FabricTextureDto;

import javax.naming.ServiceUnavailableException;
import javax.validation.constraints.NotNull;

@CircuitBreaker(name = "fabric-texture-cb")
@ReactiveFeignClient(name = "fabric-texture-service", fallback = FabricTextureClient.Fallback.class)
public interface FabricTextureClient {
    @GetMapping("/api/v1/fabric-textures/{fabricTextureId}")
    Mono<FabricTextureDto> getFabricTextureById(@NotNull @PathVariable Long fabricTextureId,
                                                @RequestHeader("Authorization") String token);

    @Component
    @Log4j2
    class Fallback implements FabricTextureClient {
        @Override
        public Mono<FabricTextureDto> getFabricTextureById(Long fabricTextureId, String token) {
            log.warn("Circuit Breaker encountered an error while fabric textures getting");
            return Mono.error(new ServiceUnavailableException("Fabric-texture-service unavailable"));
        }
    }
}
