package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.dto.fabric_texture.CreateFabricTextureDto;
import ru.se.ifmo.tinder.dto.fabric_texture.FabricTextureDto;
import ru.se.ifmo.tinder.dto.location.CreateLocationDto;
import ru.se.ifmo.tinder.dto.location.LocationDto;
import ru.se.ifmo.tinder.service.FabricTextureService;
import ru.se.ifmo.tinder.service.LocationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fabric-texture")
@SecurityRequirement(name = "basicAuth")
public class FabricTextureController {

    private final FabricTextureService fabricTextureService;

    @PostMapping
    public ResponseEntity<FabricTextureDto> createFabricTexture(@Valid @RequestBody CreateFabricTextureDto dto) {
        FabricTextureDto fabricTextureDto = fabricTextureService.createFabricTexture(dto);
        return new ResponseEntity<>(fabricTextureDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<FabricTextureDto> updateFabricTexture(@Valid @RequestBody FabricTextureDto dto) {
        FabricTextureDto fabricTextureDto = fabricTextureService.updateFabricTexture(dto);
        return ResponseEntity.ok(fabricTextureDto);
    }

    @DeleteMapping("{fabricTextureId}")
    public ResponseEntity<Void> deleteLocationById(@NotNull @PathVariable Long fabricTextureId) {
        fabricTextureService.deleteLocationById(fabricTextureId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{fabricTextureId}")
    public ResponseEntity<FabricTextureDto> getFabricTextureById(@NotNull @PathVariable Long fabricTextureId) {
        FabricTextureDto fabricTextureDto = fabricTextureService.getFabricTextureDtoById(fabricTextureId);
        return ResponseEntity.ok(fabricTextureDto);
    }

    @GetMapping
    public ResponseEntity<Page<FabricTextureDto>> getAllFabricTexture(@RequestParam int page,
                                                                 @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FabricTextureDto> fabricTexturePage = fabricTextureService.getAllFabricTexture(pageable);
        return ResponseEntity.ok(fabricTexturePage);
    }
}
