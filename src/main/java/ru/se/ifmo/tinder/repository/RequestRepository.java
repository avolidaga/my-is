package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<UserRequest,Integer> {


    @Transactional
    @Query(value = "SELECT * FROM GetAllUserRequest()", nativeQuery = true)
    List<Integer> getAllUserRequest();


    @Transactional
    @Query(value = "SELECT * FROM user_request WHERE user_request_id IN (:idList)", nativeQuery = true)
    List<UserRequest> getListAllByUserRequestIdIn(List<Integer> idList);




//    @Transactional
//    @Query(value = "SELECT * FROM insert_shooting_data(:username, :isKronbars)", nativeQuery = true)
//    Long insertShootingData(@Param("username") String username, @Param("isKronbars") boolean isKronbars);


//    getAllUserRequest
//getListAllByUserRequestIdIn
}
