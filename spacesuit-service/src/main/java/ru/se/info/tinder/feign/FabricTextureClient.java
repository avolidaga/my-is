package ru.se.info.tinder.feign;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.se.info.tinder.dto.FabricTextureDto;

@FeignClient("fabric-texture-service")
public interface FabricTextureClient {
    @GetMapping("/api/v1/fabric-textures/{fabricTextureId}")
    ResponseEntity<FabricTextureDto> getFabricTextureById(@NotNull @PathVariable Long fabricTextureId);
}
