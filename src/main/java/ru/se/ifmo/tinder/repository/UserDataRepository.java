package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.dto.UserDataDto;
import ru.se.ifmo.tinder.model.UserData;

import java.util.List;

@Repository
public interface UserDataRepository extends JpaRepository<UserData,Integer> {
    @Transactional
    @Query(value = "SELECT * FROM GetUserDataOnEarth()", nativeQuery = true)
    List<UserDataDto> getUserDataOnEarth();

    @Transactional
    @Query(value = "SELECT * FROM GetUserDataOnMars()", nativeQuery = true)
    List<Integer> getUserDataOnMars();

    @Transactional
    @Query(value = "SELECT * FROM user_data WHERE user_data_id IN (:idList)", nativeQuery = true)
    List<UserData> getListAllByUserDataIdIn(List<Integer> idList);
}

