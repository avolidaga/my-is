package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.model.UserConnection;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, Integer> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_connections (user_id_1, user_id_2) VALUES (:userId1, :userId2) RETURNING id", nativeQuery = true)
    Integer insertUserConnection(Integer userId1, Integer userId2);
}

