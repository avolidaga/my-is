package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.model.Shooting;

@Repository

public interface ShootingRepository extends JpaRepository<Shooting,Long> {

    @Transactional
    @Query(value = "SELECT * FROM insert_shooting_data(:username, :isKronbars)", nativeQuery = true)
    Long insertShootingData(@Param("username") String username, @Param("isKronbars") boolean isKronbars);
}
