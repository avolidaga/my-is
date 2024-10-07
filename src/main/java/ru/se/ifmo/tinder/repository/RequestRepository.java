package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.model.UserRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<UserRequest,Integer> {

    @Transactional
    @Query(name = "UserRequest.getAllUserRequestIds")
    List<Integer> getAllUserRequestIds();

    @Transactional
    @Query(name = "UserRequest.getDeclinedUserRequestIds")
    List<Integer> getDeclinedUserRequestIds();

    @Transactional
    @Query(name = "UserRequest.getReadyUserRequest")
    List<Integer> getReadyUserRequest();

    @Transactional
    @Query(name = "UserRequest.getInProgressUserRequest")
    List<Integer> getInProgressUserRequest();

    @Transactional
    @Query(name = "UserRequest.getListAllByUserRequestIdIn")
    List<UserRequest> getListAllByUserRequestIdIn(@Param("idList") List<Integer> idList);


}
