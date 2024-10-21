package ru.se.ifmo.tinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.se.ifmo.tinder.model.UserSpacesuitData;

import java.util.List;

@Repository
public interface UserSpacesuitDataRepository extends JpaRepository<UserSpacesuitData, Long> {
    @Query(value = "SELECT * FROM user_spacesuit_data WHERE user_spacesuit_data_id IN (:idList)", nativeQuery = true)
    Page<UserSpacesuitData> getListAllByUserSpacesuitDataIdIn(@Param("idList") List<Long> idList, Pageable pageable);
    Page<UserSpacesuitData> findAllUserSpacesuitDataByOwnerUserId(Long userId, Pageable pageable);
}
