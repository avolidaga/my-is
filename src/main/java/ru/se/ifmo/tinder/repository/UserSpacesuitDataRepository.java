package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.se.ifmo.tinder.model.UserSpacesuitData;

import java.util.List;


@Repository
public interface UserSpacesuitDataRepository extends JpaRepository<UserSpacesuitData, Integer> {

    @Query(value = "SELECT * FROM user_spacesuit_data WHERE user_spacesuit_data_id IN (:idList)", nativeQuery = true)
    List<UserSpacesuitData> getListAllByUserSpacesuitDataIdIn(List<Integer> idList);

}
