package org.akhil.authorizationserver.repository;

import org.akhil.authorizationserver.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser , Integer> {

    Optional<AppUser> findByUsername(String username);
}
