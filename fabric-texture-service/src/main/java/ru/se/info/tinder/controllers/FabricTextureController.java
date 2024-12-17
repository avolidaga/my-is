package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.FabricTextureDto;
import ru.se.info.tinder.dto.RequestFabricTextureDto;
import ru.se.info.tinder.service.FabricTextureService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fabric-textures")
public class FabricTextureController {

    private final FabricTextureService fabricTextureService;

    @PostMapping("new")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<FabricTextureDto> createFabricTexture(@Valid @RequestBody RequestFabricTextureDto dto) {
        return fabricTextureService.createFabricTexture(dto);
    }

    @PutMapping("{fabricTextureId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<FabricTextureDto> updateFabricTexture(@NotNull @PathVariable Long fabricTextureId,
                                                      @Valid @RequestBody RequestFabricTextureDto dto) {
        return fabricTextureService.updateFabricTextureById(fabricTextureId, dto);
    }

    @DeleteMapping("{fabricTextureId}")
    @PreAuthorize("hasAuthority('ADMIN')")
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
    public Flux<FabricTextureDto> getAllFabricTextures(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return fabricTextureService.getAllFabricTextures()
                .skip((long) page * size)
                .take(size);
    }
}
