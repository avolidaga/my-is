package ru.se.info.tinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.se.info.tinder.model.UserRequest;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    @Query(name = "UserRequest.getAllUserRequestIds")
    Page<UserRequest> findAll(Pageable pageable);

    @Query(name = "UserRequest.getDeclinedUserRequestIds")
    Page<UserRequest> findDeclined(Pageable pageable);

    @Query(name = "UserRequest.getReadyUserRequest")
    Page<UserRequest> findReady(Pageable pageable);

    @Query(name = "UserRequest.getInProgressUserRequest")
    Page<UserRequest> findInProgress(Pageable pageable);

    @Query(name = "UserRequest.getNewUserRequest")
    Page<UserRequest> findNew(Pageable pageable);
}
