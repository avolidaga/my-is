package ru.se.ifmo.tinder.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserSpacesuitData;

import java.util.List;


@Repository
public interface UserSpacesuitDataRepository extends JpaRepository<UserSpacesuitData,Integer> {


    @Transactional
    @Query(value = "SELECT * FROM insert_user_spacesuit_data(:head, :chest, :waist, :hips, :foot_size, :height, :fabric_texture_id, :user_id)", nativeQuery = true)
    Integer insertUserSpacesuitData(@Param("head") int head, @Param("chest") int chest,
                                    @Param("waist") int waist, @Param("hips") int hips,
                                    @Param("foot_size") int foot_size, @Param("height") int height,
                                    @Param("fabric_texture_id") int fabric_texture_id, @Param("user_id") Integer user_id);


    //getListAllByUserSpacesuitDataIdIn
//getCurrUserSpacesuitData


    @Transactional
    @Query(value = "SELECT * FROM getCurrUserSpacesuitData(:user_id)", nativeQuery = true)
    List<Integer> getCurrUserSpacesuitData(Integer user_id);


    @Transactional
    @Query(value = "SELECT * FROM user_spacesuit_data WHERE user_spacesuit_data_id IN (:idList)", nativeQuery = true)
    List<UserSpacesuitData> getListAllByUserSpacesuitDataIdIn(List<Integer> idList);

}
