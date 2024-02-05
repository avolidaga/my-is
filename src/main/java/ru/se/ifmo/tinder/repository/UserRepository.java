package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.model.User;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);



    @Transactional
    @Query(value = "SELECT * FROM insert_user_connection(:user_id_1, :user_id_2)", nativeQuery = true)
    Integer addUserConnection(@Param("user_id_1") Integer user_id_1, @Param("user_id_2") Integer user_id_2);



// нам нужно получить List всех людей, у которыч connection с текущим пользователем совпадает


    @Transactional
    @Query(value = "SELECT * FROM GetAllConnectionsForUser(:user_id_1)", nativeQuery = true)
    List<Integer> getUsersIdConnection(Integer user_id_1);

//    @Transactional
//    @Query(value = "SELECT user_data_id FROM user_data WHERE user_id IN (:idList)", nativeQuery = true)
//    List<Integer> getListAllUserById(List<Integer> idList);


    @Transactional
    @Query(value = "SELECT id FROM users WHERE user_data_id=(:userDataId)", nativeQuery = true)
    Integer findUserByUserDataId(Integer userDataId);

}



