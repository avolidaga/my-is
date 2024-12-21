package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
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
@OpenAPIDefinition(
        servers = {@Server(url = "http://localhost:8080")}
)
public class FabricTextureController {

    private final FabricTextureService fabricTextureService;

    @PostMapping("new")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Create a new fabric texture",
            description = "Create a new fabric texture with the provided details.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fabric texture created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request due to validation errors")
    })
    public Mono<FabricTextureDto> createFabricTexture(@Valid @RequestBody RequestFabricTextureDto dto) {
        return fabricTextureService.createFabricTexture(dto);
    }

    @PutMapping("{fabricTextureId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Update an existing fabric texture",
            description = "Update a fabric texture by its ID with the provided details.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fabric texture updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid data"),
            @ApiResponse(responseCode = "404", description = "Fabric texture not found")
    })
    public Mono<FabricTextureDto> updateFabricTexture(@NotNull @PathVariable Long fabricTextureId,
                                                      @Valid @RequestBody RequestFabricTextureDto dto) {
        return fabricTextureService.updateFabricTextureById(fabricTextureId, dto);
    }

    @DeleteMapping("{fabricTextureId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Delete a fabric texture",
            description = "Delete a fabric texture by its ID.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fabric texture deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Fabric texture not found")
    })
    public Mono<Void> deleteFabricTextureById(@NotNull @PathVariable Long fabricTextureId) {
        return fabricTextureService.deleteFabricTextureById(fabricTextureId);
    }

    @GetMapping("{fabricTextureId}")
    @Operation(
            summary = "Get fabric texture by ID",
            description = "Retrieve the details of a fabric texture by its ID.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fabric texture found"),
            @ApiResponse(responseCode = "404", description = "Fabric texture not found")
    })
    public Mono<FabricTextureDto> getFabricTextureById(@NotNull @PathVariable Long fabricTextureId) {
        return fabricTextureService.getFabricTextureById(fabricTextureId);
    }

    @GetMapping
    @Operation(
            summary = "Get all fabric textures",
            description = "Retrieve a paginated list of all fabric textures.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of fabric textures"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public Flux<FabricTextureDto> getAllFabricTextures(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return fabricTextureService.getAllFabricTextures()
                .skip((long) page * size)
                .take(size);
    }
}
