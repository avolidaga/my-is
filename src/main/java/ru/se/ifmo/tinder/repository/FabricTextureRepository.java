package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.se.ifmo.tinder.model.FabricTexture;

@Repository
public interface FabricTextureRepository extends JpaRepository<FabricTexture, Long> {
}
