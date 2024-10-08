package ru.se.ifmo.tinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.model.UserSpacesuitData;

import java.util.List;

@Repository
public interface UserSpacesuitDataRepository extends JpaRepository<UserSpacesuitData, Integer> {

    @Query(value = "SELECT * FROM user_spacesuit_data WHERE user_spacesuit_data_id IN (:idList)", nativeQuery = true)
    Page<UserSpacesuitData> getListAllByUserSpacesuitDataIdIn(@Param("idList") List<Integer> idList, Pageable pageable);
}
