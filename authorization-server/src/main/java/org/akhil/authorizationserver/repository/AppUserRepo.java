package org.akhil.authorizationserver.repository;

import jakarta.transaction.Transactional;
import org.akhil.authorizationserver.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser , Integer> {

    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    Boolean existsByEmail(String email);
    AppUser findByEmailIgnoreCase(String emailId);

    @Transactional
    @Modifying
    @Query("update AppUser u set u.password = ?2 where u.email =?1")
    void updatePassword(String email,String password);

}
