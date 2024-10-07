package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.se.ifmo.tinder.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);

//    @Transactional
//    @Query(value = "SELECT user_data_id FROM user_data WHERE user_id IN (:idList)", nativeQuery = true)
//    List<Integer> getListAllUserById(List<Integer> idList);

    @Transactional
    @Query(value = "SELECT id FROM users WHERE user_data_id=(:userDataId)", nativeQuery = true)
    Integer findUserByUserDataId(Integer userDataId);
}
