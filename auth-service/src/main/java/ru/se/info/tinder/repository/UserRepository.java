package ru.se.info.tinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.se.info.tinder.model.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    @Query(value = "SELECT u.user_spacesuit_data_id FROM users u WHERE u.id = :user_id", nativeQuery = true)
    Page<Integer> getSpacesuitDataByUserId(@Param("user_id") Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM users WHERE user_data_id=(:userDataId)", nativeQuery = true)
    Optional<UserEntity> findUserByUserDataId(Long userDataId);
}
