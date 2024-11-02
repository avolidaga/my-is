package ru.se.info.tinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.se.ifmo.tinder.model.UserData;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    @Query("SELECT u FROM UserData u WHERE u.id <> ?1")
    Page<UserData> findAllUserDataExcludingUserId(Long userDataId, Pageable pageable);

    Page<UserData> findAllUserDataByLocationsId(Long locationId, Pageable pageable);
}