package ru.se.info.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.se.info.tinder.model.Roles;
import ru.se.info.tinder.model.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Roles findRolesByRoleName(RoleName roleName);
}
