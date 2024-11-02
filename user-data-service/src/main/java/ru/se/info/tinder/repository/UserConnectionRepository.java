package ru.se.info.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.se.ifmo.tinder.model.UserConnection;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, Long> {

}
