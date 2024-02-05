package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.se.ifmo.tinder.model.Roles;

/**
 * @author amifideles
 */
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Roles findRolesByRoleName(String roleName);
}
