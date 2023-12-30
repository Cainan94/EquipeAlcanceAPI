package com.cbt.CAJsSystem.modules.roles.repopsitory;

import com.cbt.CAJsSystem.modules.roles.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolesRepository extends JpaRepository<Roles, UUID> {
    Optional<Roles> findByRoleName(String name);
    Optional<Roles> findByRoleCode(Integer code);
}
