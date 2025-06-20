package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.InfoDto;
import ru.se.info.tinder.dto.RequestInfoDto;
import ru.se.info.tinder.service.InfoService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/info")
@OpenAPIDefinition(
        servers = {@Server(url = "http://localhost:8080")}
)
public class InfoController {

    private final InfoService infoService;

    @PostMapping("new")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<InfoDto> createInfo(@Valid @RequestBody RequestInfoDto dto) {
        return infoService.createInfo(dto);
    }

    @PutMapping("{infoId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<InfoDto> updateInfo(@NotNull @PathVariable Long infoId,
                                            @Valid @RequestBody RequestInfoDto dto) {
        return infoService.updateInfoById(infoId, dto);
    }

    @DeleteMapping("{infoId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<Void> deleteInfoById(@NotNull @PathVariable Long infoId) {
        return infoService.deleteInfoById(infoId);
    }

    @GetMapping("{infoId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<InfoDto> getInfoById(@NotNull @PathVariable Long infoId) {
        return infoService.getInfoDtoById(infoId);
    }

    @GetMapping("list")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Flux<InfoDto> getInfosByIds(@NotNull @RequestParam List<Long> infoIds) {
        return infoService.getInfosListByIds(Flux.fromIterable(infoIds));
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Flux<InfoDto> getAllInfos(@RequestParam(defaultValue = "0") @Min(0) int page,
                                             @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return infoService.getAllInfos()
                .skip((long) page * size)
                .take(size);
    }
}
