package ru.se.info.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.se.info.tinder.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
