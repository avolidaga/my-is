package ru.se.info.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.se.info.tinder.model.UserRequest;

import java.util.List;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    @Query(name = "UserRequest.getAllUserRequestIds")
    List<UserRequest> findAll();

    @Query(name = "UserRequest.getDeclinedUserRequestIds")
    List<UserRequest> findDeclined();

    @Query(name = "UserRequest.getReadyUserRequest")
    List<UserRequest> findReady();

    @Query(name = "UserRequest.getInProgressUserRequest")
    List<UserRequest> findInProgress();

    @Query(name = "UserRequest.getNewUserRequest")
    List<UserRequest> findNew();
}
