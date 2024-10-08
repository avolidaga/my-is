package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.se.ifmo.tinder.model.Roles;
import ru.se.ifmo.tinder.model.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Roles findRolesByRoleName(RoleName roleName);
}
