package ru.se.info.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.se.info.tinder.entity.ProfileImage;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
