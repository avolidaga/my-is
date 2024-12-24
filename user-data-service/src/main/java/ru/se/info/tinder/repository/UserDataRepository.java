package ru.se.info.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.se.info.tinder.model.UserData;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    List<UserData> findAllUserDataByLocationsId(Long locationId);

    @Query("SELECT u FROM UserData u WHERE u.ownerUser.username = ?1")
    Optional<UserData> findUserDataByUsername(String username);
}