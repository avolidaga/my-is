package ru.se.ifmo.tinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.se.ifmo.tinder.model.UserRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<UserRequest, Integer> {
    //пагинация
    @Query(name = "UserRequest.getAllUserRequestIds")
    Page<UserRequest> findAll(Pageable pageable);

    @Query(name = "UserRequest.getDeclinedUserRequestIds")
    Page<UserRequest> findDeclined(Pageable pageable);

    @Query(name = "UserRequest.getReadyUserRequest")
    Page<UserRequest> findReady(Pageable pageable);

    @Query(name = "UserRequest.getInProgressUserRequest")
    Page<UserRequest> findInProgress(Pageable pageable);

    @Query(name = "UserRequest.getNewUserRequest")
    List<Integer> getNewUserRequestIds(Pageable pageable);

    @Query(name = "UserRequest.getListAllByUserRequestIdIn")
    Page<UserRequest> getListAllByUserRequestIdIn(@Param("idList") List<Integer> idList, Pageable pageable);

}
