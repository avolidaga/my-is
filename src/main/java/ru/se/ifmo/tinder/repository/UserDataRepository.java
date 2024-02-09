package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.dto.UserDataDto;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.enums.Location;
import ru.se.ifmo.tinder.model.enums.Sex;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserDataRepository extends JpaRepository<UserData,Integer> {
    @Transactional
    @Query(value = "SELECT * FROM GetUserDataOnEarth()", nativeQuery = true)
    List<Integer> getUserDataOnEarth();

    @Transactional
    @Query(value = "SELECT * FROM GetUserDataOnMars()", nativeQuery = true)
    List<Integer> getUserDataOnMars();


    @Transactional
    @Query(value = "SELECT * FROM user_data WHERE user_data_id IN (:idList)", nativeQuery = true)
    List<UserData> getListAllByUserDataIdIn(List<Integer> idList);

    @Transactional
    @Query(value = "SELECT * FROM GetAllUserData(:user_id)", nativeQuery = true)
    List<Integer> getAllUserData(Integer user_id);



    @Transactional
    @Query(value = "SELECT * FROM insert_user_data(:birth_date, CAST(:sex AS sex_enum), :weight, :height, :hair_color, CAST(:location AS location_enum),:firstname,  :user_id)", nativeQuery = true)
    Integer insertUserData(@Param("birth_date") LocalDate birth_date, @Param("sex") String sex, @Param("weight") int weight,
                           @Param("height") int height, @Param("hair_color") String hair_color, @Param("location") String location,@Param("firstname") String firstname,  @Param("user_id") Integer user_id);

    @Transactional
    @Query(value = "SELECT * FROM user_data WHERE user_data_id IN (SELECT user_data_id FROM users WHERE id IN (:idList))", nativeQuery = true)
    List<UserData> getListAllByUserDataIdInForConnections(List<Integer> idList);

    //getCurrUserData

    @Transactional
    @Query(value = "SELECT * FROM getCurrUserData(:user_id)", nativeQuery = true)
    List<Integer> getCurrUserData(Integer user_id);

}