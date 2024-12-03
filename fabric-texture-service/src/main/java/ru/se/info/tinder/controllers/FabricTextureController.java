package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.FabricTextureDto;
import ru.se.info.tinder.dto.RequestFabricTextureDto;
import ru.se.info.tinder.service.FabricTextureService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fabric-textures")
public class FabricTextureController {

    private final FabricTextureService fabricTextureService;

    @PostMapping("new")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<FabricTextureDto> createFabricTexture(@Valid @RequestBody RequestFabricTextureDto dto) {
        return fabricTextureService.createFabricTexture(dto);
    }

    @PutMapping("{fabricTextureId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<FabricTextureDto> updateFabricTexture(@NotNull @PathVariable Long fabricTextureId,
                                                      @Valid @RequestBody RequestFabricTextureDto dto) {
        return fabricTextureService.updateFabricTextureById(fabricTextureId, dto);
    }

    @DeleteMapping("{fabricTextureId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<Void> deleteFabricTextureById(@NotNull @PathVariable Long fabricTextureId) {
        return fabricTextureService.deleteFabricTextureById(fabricTextureId);
    }

    @GetMapping("{fabricTextureId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<FabricTextureDto> getFabricTextureById(@NotNull @PathVariable Long fabricTextureId) {
        return fabricTextureService.getFabricTextureById(fabricTextureId);
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Flux<FabricTextureDto> getAllFabricTextures(@RequestParam int page,
                                                       @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fabricTextureService.getAllFabricTextures(pageable);
    }
}
