package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.dto.UserDataDto;
import ru.se.ifmo.tinder.model.UserData;

import java.util.List;

@Repository
public interface UserDataRepository extends JpaRepository<UserData,Long> {
    @Transactional
    @Query(value = "SELECT * FROM GetUserDataOnEarth()", nativeQuery = true)
    List<UserDataDto> getUserDataOnEarth();

    @Transactional
    @Query(value = "SELECT * FROM GetUserDataOnMars()", nativeQuery = true)
    List<UserDataDto> getUserDataOnMars();
}

