package ru.se.info.tinder.repository;

import org.springframework.stereotype.Repository;
import ru.se.info.tinder.model.FabricTexture;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

@Repository
public interface FabricTextureRepository extends R2dbcRepository<FabricTexture, Long> {
}
