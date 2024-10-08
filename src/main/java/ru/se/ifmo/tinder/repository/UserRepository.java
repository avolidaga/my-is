package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.se.ifmo.tinder.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u.user_spacesuit_data_id FROM users u WHERE u.id = :user_id", nativeQuery = true)
    List<Integer> getCurrUserSpacesuitData(@Param("user_id") Integer userId);


//    @Transactional
//    @Query(value = "SELECT user_data_id FROM user_data WHERE user_id IN (:idList)", nativeQuery = true)
//    List<Integer> getListAllUserById(List<Integer> idList);

    @Query(value = "SELECT * FROM users WHERE user_data_id=(:userDataId)", nativeQuery = true)
    Optional<User> findUserByUserDataId(Integer userDataId);
}
