package ru.se.info.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.se.info.tinder.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
