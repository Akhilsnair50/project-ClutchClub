package org.akhil.authorizationserver.repository;

import org.akhil.authorizationserver.enums.RoleName;
import org.akhil.authorizationserver.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByRole(RoleName roleName);
}
