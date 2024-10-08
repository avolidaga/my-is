package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.se.ifmo.tinder.model.UserRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<UserRequest, Integer> {

    @Query(name = "UserRequest.getAllUserRequestIds")
    List<Integer> getAllUserRequestIds();

    @Query(name = "UserRequest.getDeclinedUserRequestIds")
    List<Integer> getDeclinedUserRequestIds();

    @Query(name = "UserRequest.getReadyUserRequest")
    List<Integer> getReadyUserRequest();

    @Query(name = "UserRequest.getInProgressUserRequest")
    List<Integer> getInProgressUserRequest();

    @Query(name = "UserRequest.getNewUserRequest")
    List<Integer> getNewUserRequestIds();

    @Query(name = "UserRequest.getListAllByUserRequestIdIn")
    List<UserRequest> getListAllByUserRequestIdIn(@Param("idList") List<Integer> idList);

}
