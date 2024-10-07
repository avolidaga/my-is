package ru.se.ifmo.tinder.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.model.UserSpacesuitData;

import java.util.List;


@Repository
public interface UserSpacesuitDataRepository extends JpaRepository<UserSpacesuitData,Integer> {

    @Transactional
    @Query(value = "SELECT usd.user_spacesuit_data_id FROM user_spacesuit_data usd WHERE usd.user_id = :user_id", nativeQuery = true)
    List<Integer> getCurrUserSpacesuitData(@Param("user_id") Integer userId);

    @Transactional
    @Query(value = "SELECT * FROM user_spacesuit_data WHERE user_spacesuit_data_id IN (:idList)", nativeQuery = true)
    List<UserSpacesuitData> getListAllByUserSpacesuitDataIdIn(List<Integer> idList);

}
