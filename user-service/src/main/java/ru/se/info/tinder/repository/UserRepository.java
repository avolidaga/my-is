package ru.se.info.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.se.info.tinder.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllUserDataByLocationsId(Long locationId);

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> findUserDataByUsername(String username);
}