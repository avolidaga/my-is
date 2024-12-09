package ru.se.info.tinder.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.FabricTextureDto;
import javax.validation.constraints.NotNull;

@CircuitBreaker(name = "fabric-texture-cb")
@ReactiveFeignClient("fabric-texture-service")
public interface FabricTextureClient {
    @GetMapping("/api/v1/fabric-textures/{fabricTextureId}")
    Mono<FabricTextureDto> getFabricTextureById(@NotNull @PathVariable Long fabricTextureId,
                                                @RequestHeader("Authorization") String token);
}
